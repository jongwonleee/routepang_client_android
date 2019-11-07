package com.itaewonproject

import android.app.Activity
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Constraints.TAG
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.itaewonproject.customviews.CustomTextView
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.mypage.RouteMapFragment

class RouteUtils(val map: GoogleMap, val fragment: RouteMapFragment) {
    val view: View
    val image: ImageView
    val latLoc = HashMap<LatLng, Location>()
    val latIndex = HashMap<LatLng, Int>()
    val latWishlist = HashMap<LatLng, Int>()
    val imageList = listOf(listOf(R.drawable.ic_map_pin_fill_blue,R.drawable.ic_map_pin_fill_green,R.drawable.ic_map_pin_fill_purple,R.drawable.ic_map_pin_fill_red,R.drawable.ic_map_pin_fill_yellow),
        listOf(R.drawable.ic_map_pin_fill_blue_minus, R.drawable.ic_map_pin_fill_green_minus, R.drawable.ic_map_pin_fill_purple_minus, R.drawable.ic_map_pin_fill_red_minus, R.drawable.ic_map_pin_fill_yellow_minus),
        listOf(R.drawable.ic_map_pin_fill_blue_plus, R.drawable.ic_map_pin_fill_green_plus, R.drawable.ic_map_pin_fill_purple_plus, R.drawable.ic_map_pin_fill_red_plus, R.drawable.ic_map_pin_fill_yellow_plus),
        listOf(R.drawable.ic_map_pin_not_fill_blue,R.drawable.ic_map_pin_not_fill_green,R.drawable.ic_map_pin_not_fill_purple,R.drawable.ic_map_pin_not_fill_red,R.drawable.ic_map_pin_not_fill_yellow))
    var editMode = false
        set(value) {
            field = value
            if (value) {
                setList()
                setWishList()
            } else {
                removeWishList()
                addLine()
            }
        }
    var geoApiContext: GeoApiContext
    var selectedMarker: Marker?
    init {
        geoApiContext = GeoApiContext.Builder().apiKey(fragment.context!!.getString(R.string.Web_key)).build()
        view = LayoutInflater.from(fragment.context).inflate(R.layout.view_route_marker, null)
        image = view.findViewById(R.id.image) as ImageView


        selectedMarker = null
        map.setOnMarkerClickListener {
            if (editMode) {
                if (it != null && selectedMarker != null) {
                    if (selectedMarker!!.position == it.position) {
                        if (latWishlist.contains(it.position)) {
                            val addLocation = fragment.wishlist[latWishlist[it.position]!!]
                            fragment.wishlist.removeAt(latWishlist[it.position]!!)
                            fragment.list.add(addLocation)
                            fragment.setAdapterList()
                            latWishlist.remove(it.position)
                        } else {
                            val deleteLocation = fragment.list[latIndex[it.position]!!]
                            fragment.list.removeAt(latIndex[it.position]!!)
                            fragment.wishlist.add(deleteLocation)
                            fragment.setAdapterList()
                            latIndex.remove(it.position)
                        }
                        setList()
                        setWishList()
                    } else changeSelectedMarker(it)
                } else
                    changeSelectedMarker(it)
            }
            return@setOnMarkerClickListener editMode
        }
        map.setOnMapClickListener({
            changeSelectedMarker(null)
        })
    }

    fun setList() {
        map.clear()
        selectedMarker = null
        var list = fragment.list
        for (l in 0..list.size - 1) {
            addMarker(list[l], false, l, false)
        }
    }

    fun setWishList() {
        setList()
        var list = fragment.wishlist
        for (l in 0..list.size - 1) {
            addMarker(list[l], false, l, true)
        }
    }

    fun removeWishList() {
        setList()
        addLine()
    }

    fun addMarker(location: Location, isSelected: Boolean, index: Int, isWishlist: Boolean): Marker {
        Log.i("adding marker","${isSelected}, ${index}, ${isWishlist}")
        val position = location.latlng()
        val categoryColor = CategoryIcon.getIndex(location.category!!)
        image.setImageResource(if(isSelected){
            if(isWishlist) imageList[2][categoryColor]
            else imageList[1][categoryColor]
        }else
        {
            if(isWishlist) imageList[3][categoryColor]
            else imageList[0][categoryColor]
        })

        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(view)))
        val marker = map.addMarker(markerOptions)
        if (isWishlist) latWishlist.put(position, index)
        else latIndex.put(position, index)
        latLoc.put(position, location)
        return marker
    }

    fun changeSelectedMarker(marker: Marker?) {
        if (selectedMarker != null) {
            addEditMarker(selectedMarker!!, false)
            selectedMarker!!.remove()
            selectedMarker=null
        }
        if (marker != null) {
            selectedMarker = addEditMarker(marker, true)!!
            marker.remove()
        }else
        {

        }
    }

    fun setBoundary(list: ArrayList<Location>) {
        val bound = LatLngBounds.builder()
        for (l in list) {
            bound.include(l.latlng())
        }
        if(list.size>0)map.moveCamera(CameraUpdateFactory.newLatLngBounds(bound.build(), 100))
    }

    private fun addEditMarker(marker: Marker, isSelected: Boolean): Marker? {
        if (latLoc.containsKey(marker.position)) {
            val location = latLoc.get(marker.position)
            if (location != null)
                if (latWishlist.containsKey(marker.position))
                    return addMarker(location, isSelected, latWishlist.get(marker.position)!!, true)
                else return addMarker(location, isSelected, latIndex.get(marker.position)!!, false)
        }
        return null
    }

    private fun createDrawableFromView(view: View): Bitmap {
        val displayMatrics = DisplayMetrics()
        (fragment.context as Activity).windowManager.defaultDisplay.getMetrics(displayMatrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.measure(displayMatrics.widthPixels, displayMatrics.heightPixels)
        view.layout(0, 0, displayMatrics.widthPixels, displayMatrics.heightPixels)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    fun addLine() {
        var list = fragment.list

        val arrayPoints = arrayListOf<LatLng>()
        for (l in list) {
            arrayPoints.add(l.latlng())
        }
        for (i in 0..arrayPoints.size - 2) {
            calculateDirections(arrayPoints[i], arrayPoints[i + 1])
        }
    }

    //FIXME:directions 저장해놓고 바뀔 시에만 하기
    private fun calculateDirections(origin: LatLng, dest: LatLng) {
        Log.d(TAG, "calculateDirections: calculating directions.")

        val destination = com.google.maps.model.LatLng(dest.latitude, dest.longitude)
        val directions = DirectionsApiRequest(geoApiContext)

        directions.alternatives(true)
        directions.mode(TravelMode.TRANSIT)
        directions.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
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

    private fun addPolylinesToMap(result: DirectionsResult) {
        Handler(Looper.getMainLooper()).post(Runnable {
            val polylineOptions = PolylineOptions()
            polylineOptions.color(Color.argb(255,253,98,176))
            polylineOptions.width(10.0f)
            polylineOptions.clickable(false)
            polylineOptions.endCap(RoundCap())
            for (route in result.routes) {
                val decodedPath = PolylineEncoding.decode(route.overviewPolyline.encodedPath)
                val list = arrayListOf<LatLng>()
                for (path in decodedPath) {
                    list.add(LatLng(path.lat, path.lng))
                }
                polylineOptions.addAll(list)
            }
            map.addPolyline(polylineOptions)
        })
    }
}
