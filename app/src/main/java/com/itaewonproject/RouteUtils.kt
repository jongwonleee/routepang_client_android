package com.itaewonproject

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints.TAG
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.google.maps.GeoApiContext
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Rating
import java.io.IOException
import com.google.maps.model.DirectionsResult
import com.google.maps.DirectionsApiRequest
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.TravelMode
import com.itaewonproject.mypage.RouteMapFragment


class RouteUtils(val map: GoogleMap, val fragment:RouteMapFragment){
    val view:View
/*    val layoutInfo:ConstraintLayout
    val buttonAdd:ImageView
    val name:TextView
    val rating:RatingBar
    val category:ImageView
    val marker:ImageView*/
    val rating:ProgressBar
    val text:CustomTextView
    val image:ImageView
    val imageAdd:ImageView
    val articleCount:ProgressBar
    val latLoc = HashMap<LatLng,Location>()
    val latIndex = HashMap<LatLng,Int>()
    val latWishlist = HashMap<LatLng,Int>()
    var editMode=false
        set(value){
            field=value
            if(value){
                setList()
                setWishList()
            }else
            {
                removeWishList()
                addLine()
            }
        }
    var geoApiContext:GeoApiContext
    var selectedMarker:Marker?
    init{
        geoApiContext = GeoApiContext.Builder().apiKey(fragment.context!!.getString(R.string.Web_key)).build()
        view = LayoutInflater.from(fragment.context).inflate(R.layout.view_route_marker,null)
        rating=view.findViewById(R.id.progressBar_rating) as ProgressBar
        text = view.findViewById(R.id.text) as CustomTextView
        image = view.findViewById(R.id.image) as ImageView
        imageAdd=view.findViewById(R.id.image_add) as ImageView
        articleCount = view.findViewById(R.id.progressBar_articleCount) as ProgressBar
        rating.max=100
        articleCount.max=20
        selectedMarker=null
        map.setOnMarkerClickListener {
            if(editMode){
                if(it!=null && selectedMarker!=null){
                    if(selectedMarker!!.position == it.position){
                        if(latWishlist.contains(it.position)){
                            val addLocation = fragment.wishlist[latWishlist[it.position]!!]
                            fragment.wishlist.removeAt(latWishlist[it.position]!!)
                            fragment.list.add(addLocation)
                        }else
                        {
                            val deleteLocation = fragment.wishlist[latIndex[it.position]!!]
                            fragment.list.removeAt(latIndex[it.position]!!)
                            fragment.wishlist.add(deleteLocation)
                        }
                        setList()
                        setWishList()
                    }else changeSelectedMarker(it!!)
                } else
                    changeSelectedMarker(it!!)
            }
            return@setOnMarkerClickListener editMode
        }
        map.setOnMapClickListener({
            changeSelectedMarker(null)
        })
    }

    fun setList(){
        map.clear()
        selectedMarker=null
        var list = fragment.list
        for(l in 0.. list.size-1){
            addMarker(list[l],false,l,false)
        }
    }

    fun setWishList(){
        setList()
        var list = fragment.wishlist
        for(l in 0.. list.size-1){
            addMarker(list[l],false,l,true)
        }
    }

    fun removeWishList(){
        setList()
        addLine()
    }

    fun addMarker(location: Location, isSelected:Boolean,index:Int,isWishlist:Boolean):Marker{
        val position = location.latlng()
        rating.progress=(location.rating*10).toInt()
        articleCount.progress= if(location.articleCount>10) 10 else location.articleCount
        Log.i("isSelected","$isSelected")
        if(isSelected){
            imageAdd.setImageResource(if(isWishlist)R.drawable.ic_add_black_24dp else R.drawable.ic_clear_black_24dp)
            imageAdd.visibility=View.VISIBLE
            text.text=""
        }else
        {
            imageAdd.visibility=View.INVISIBLE
            text.text=if(isWishlist) "" else(index+1).toString()
        }

        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFroview(view)))
        val marker = map.addMarker(markerOptions)
        if(isWishlist) latWishlist.put(position,index)
        else latIndex.put(position,index)
        latLoc.put(position,location)
        return marker
    }



    fun changeSelectedMarker(marker:Marker?){
        if(selectedMarker!=null){
            addEditMarker(selectedMarker!!,false)
            selectedMarker!!.remove()
        }
        if(marker!=null){
            selectedMarker = addEditMarker(marker,true)!!
            marker.remove()
        }
    }

    fun setBoundary(list:ArrayList<Location>){
        val bound = LatLngBounds.builder()
        for(l in list){
            bound.include(l.latlng())
        }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bound.build(),100))

    }

    private fun addEditMarker(marker:Marker,isSelected: Boolean):Marker?{
        if(latLoc.containsKey(marker.position)){
            val location = latLoc.get(marker.position)
            if(location!=null)
                if(latWishlist.containsKey(marker.position))
                    return addMarker(location,isSelected,latWishlist.get(marker.position)!!,true)
                else return addMarker(location,isSelected,latIndex.get(marker.position)!!,false)
        }
        return null
    }
    private fun getCategoryColor(category:Int): Int {
        var color = Color.GREEN
        when(category){
            0 -> color=Color.CYAN
            1 -> color=Color.GREEN
            2 -> color=Color.MAGENTA
            3 -> color=Color.RED
            4 -> color=Color.YELLOW
        }
        return color
    }
    private fun createDrawableFroview(view: View):Bitmap{
        val displayMatrics = DisplayMetrics()
        (fragment.context as Activity).windowManager.defaultDisplay.getMetrics(displayMatrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        view.measure(displayMatrics.widthPixels,displayMatrics.heightPixels)
        view.layout(0,0,displayMatrics.widthPixels,displayMatrics.heightPixels)
        view.buildDrawingCache()
        val bitmap =Bitmap.createBitmap(view.measuredWidth,view.measuredHeight,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun addLine(){
        var list = fragment.list


        val arrayPoints = arrayListOf<LatLng>()
        for(l in list){
            arrayPoints.add(l.latlng())
        }
        //calculateDirections(arrayPoints[0],arrayPoints[1])
        for(i in 0.. arrayPoints.size-2){
            calculateDirections(arrayPoints[i],arrayPoints[i+1])
        }
        //calculateDirectionList(list)

    }


    private fun calculateDirections(origin:LatLng,dest:LatLng) {
        Log.d(TAG, "calculateDirections: calculating directions.")

        val destination = com.google.maps.model.LatLng(dest.latitude, dest.longitude)
        val directions = DirectionsApiRequest(geoApiContext)

        directions.alternatives(true)
        directions.mode(TravelMode.TRANSIT)
        directions.origin(com.google.maps.model.LatLng(origin.latitude,origin.longitude))
        Log.d(TAG, "calculateDirections: destination: $destination")
        directions.destination(destination)
            .setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {
                    //                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                    //                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                    //                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                    //                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                    Log.d(TAG, "onResult: successfully retrieved directions.")
                    addPolylinesToMap(result)
                }

                override fun onFailure(e: Throwable) {
                    Log.e(TAG, "calculateDirections: Failed to get directions: " + e.message)

                }
            })
    }


    private fun addPolylinesToMap(result:DirectionsResult){
        Handler(Looper.getMainLooper()).post(Runnable {
            val polylineOptions = PolylineOptions()
            polylineOptions.color(Color.argb(96,255,0,0))
            polylineOptions.width(15.0f)
            polylineOptions.clickable(false)
            polylineOptions.endCap(RoundCap())
            for(route in result.routes){
                val decodedPath  = PolylineEncoding.decode(route.overviewPolyline.encodedPath)
                val list = arrayListOf<LatLng>()
                for(path in decodedPath){
                    list.add(LatLng(path.lat,path.lng))
                }
                polylineOptions.addAll(list)
            }
            map.addPolyline(polylineOptions)
        })
    }

}