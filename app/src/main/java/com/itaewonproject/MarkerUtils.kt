package com.itaewonproject

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Rating
import java.io.IOException

class MarkerUtils(val map: GoogleMap,val con:Context){
    val mView:View
    val layoutInfo:ConstraintLayout
    val buttonAdd:ImageView
    val name:TextView
    val rating:RatingBar
    val category:ImageView
    val marker:ImageView
    val markerInfo = HashMap<Marker,Location>()
    var selectedMarker:Marker?
    init{
        mView = LayoutInflater.from(con).inflate(R.layout.view_marker,null)
        layoutInfo = mView.findViewById(R.id.layout_info) as ConstraintLayout
        buttonAdd = mView.findViewById(R.id.button_add) as ImageView
        name = mView.findViewById(R.id.name) as TextView
        rating = mView.findViewById(R.id.rating) as RatingBar
        category = mView.findViewById(R.id.category) as ImageView
        marker=mView.findViewById(R.id.marker) as ImageView
        selectedMarker=null

        map.setOnMarkerClickListener {
            if(it!=null && selectedMarker!=null){
                if(selectedMarker!!.position == it.position){
                    changeSelectedMarker(null,false)
                    selectedMarker=null
                    return@setOnMarkerClickListener true

                }
            }
            changeSelectedMarker(it!!,false)
            return@setOnMarkerClickListener true
        }
            
    }

    fun addLocationMarker(location: Location, isSelected:Boolean):Marker{
         val position = location.latlng()
        rating.rating=location.rating
        name.text=location.name
        val markerImage = marker.drawable
        val duffColorFilter  = PorterDuffColorFilter(getCategoryColor(location.cate), PorterDuff.Mode.SRC_ATOP)
        markerImage.colorFilter=duffColorFilter
        marker.setImageDrawable(markerImage)
        buttonAdd.visibility=View.GONE
        if(isSelected){
            layoutInfo.visibility=View.VISIBLE
        }else
        {
            layoutInfo.visibility=View.INVISIBLE
        }
        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mView)))
        val marker = map.addMarker(markerOptions)
        markerInfo.put(marker,location)
        return marker
    }


    fun addEditMarker(location: Location, isSelected:Boolean):Marker{
        val position = location.latlng()
        rating.rating=location.rating
        name.text=location.name
        val markerImage = marker.drawable
        val duffColorFilter  = PorterDuffColorFilter(getCategoryColor(location.cate), PorterDuff.Mode.SRC_ATOP)
        markerImage.colorFilter=duffColorFilter
        marker.setImageDrawable(markerImage)
        buttonAdd.visibility=View.VISIBLE
        if(isSelected){
            layoutInfo.visibility=View.VISIBLE
            buttonAdd.visibility=View.VISIBLE
        }else
        {
            layoutInfo.visibility=View.INVISIBLE
            buttonAdd.visibility=View.INVISIBLE
        }
        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mView)))
        val marker = map.addMarker(markerOptions)
        markerInfo.put(marker,location)
        return marker
    }

    fun addLine(list:ArrayList<Location>){
        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.RED)
        polylineOptions.width(15.0f)
        polylineOptions.clickable(false)
        polylineOptions.endCap(RoundCap())

        val arrayPoints = arrayListOf<LatLng>()
        for(l in list){
            arrayPoints.add(l.latlng())
        }
        polylineOptions.addAll(arrayPoints)
        map.addPolyline(polylineOptions)
    }

    fun changeSelectedMarker(marker:Marker?,isEditmode:Boolean){
        if(isEditmode){
            if(selectedMarker!=null){
                addEditMarker(selectedMarker!!,false)
                selectedMarker!!.remove()
            }
            if(marker!=null){
                selectedMarker = addEditMarker(marker,true)!!
                marker.remove()
            }
        }else
        {
            if(selectedMarker!=null){
                addLocationMarker(selectedMarker!!,false)
                selectedMarker!!.remove()
            }
            if(marker!=null){
                selectedMarker = addLocationMarker(marker,true)!!
                marker.remove()
            }
        }

    }

    private fun addEditMarker(marker:Marker,isSelected: Boolean):Marker?{
        if(markerInfo.containsKey(marker)){
            val location = markerInfo.get(marker)
            if(location!=null)
                return addEditMarker(location,isSelected)
        }
        return null
    }
    private fun addLocationMarker(marker:Marker,isSelected: Boolean):Marker?{
        if(markerInfo.containsKey(marker)){
            val location = markerInfo.get(marker)
            if(location!=null)
                return addLocationMarker(location,isSelected)
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
    private fun createDrawableFromView(view: View):Bitmap{
        val displayMatrics = DisplayMetrics()
        (con as Activity).windowManager.defaultDisplay.getMetrics(displayMatrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        view.measure(displayMatrics.widthPixels,displayMatrics.heightPixels)
        view.layout(0,0,displayMatrics.widthPixels,displayMatrics.heightPixels)
        view.buildDrawingCache()
        val bitmap =Bitmap.createBitmap(view.measuredWidth,view.measuredHeight,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}