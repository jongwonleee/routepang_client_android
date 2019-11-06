package com.itaewonproject

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.itaewonproject.customviews.CustomTextView
import com.itaewonproject.model.receiver.Location

class MarkerUtils(val map: GoogleMap, val con: Context) {
    val view: View
    val image: ImageView
    val latLoc = HashMap<LatLng, Location>()
    val latIndex = HashMap<LatLng, Int>()
    var selectedMarker: Marker?
    init {
        view = LayoutInflater.from(con).inflate(R.layout.view_route_marker, null)
        image = view.findViewById(R.id.image) as ImageView
        selectedMarker = null
        map.setOnMarkerClickListener {
            if (it != null && selectedMarker != null) {
                if (selectedMarker!!.position == it.position) {
                    changeSelectedMarker(null)
                    selectedMarker = null
                } else {
                    changeSelectedMarker(it)
                }
            } else {
                changeSelectedMarker(it)
            }
            return@setOnMarkerClickListener true
        }
    }

    fun addLocationMarker(location: Location, isSelected: Boolean): Marker {
        Log.i("!!!!!","~${location.coordinates}~ ${location.name}")
        val position = location.latlng()
        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFroview(view)))
        val marker = map.addMarker(markerOptions)
        latLoc.put(position, location)
        return marker
    }

    fun addEditMarker(location: Location, isSelected: Boolean, index: Int): Marker {
        val position = location.latlng()
        Log.i("isSelected", "$isSelected")
/*
        if (isSelected) {
            text.text = "-"
        } else {
            text.text = (index + 1).toString()
        }
*/

       /* rating.rating=location.rating
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
        }*/
        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFroview(view)))
        val marker = map.addMarker(markerOptions)
        latLoc.put(position, location)
        latIndex.put(position, index)
        return marker
    }

    fun changeSelectedMarker(marker: Marker?) {
        if (selectedMarker != null) {
            addLocationMarker(selectedMarker!!, false)
            selectedMarker!!.remove()
        }
        if (marker != null) {
            selectedMarker = addLocationMarker(marker, true)!!
            marker.remove()
        }
    }

    fun setBoundary(list: ArrayList<Location>): LatLngBounds {
        val bound = LatLngBounds.builder()
        for (l in list) {
            bound.include(l.latlng())
        }
        return bound.build()
    }

    private fun addLocationMarker(marker: Marker, isSelected: Boolean): Marker? {
        if (latLoc.containsKey(marker.position)) {
            val location = latLoc.get(marker.position)
            if (location != null)
                return addLocationMarker(location, isSelected)
        }
        return null
    }
    private fun getCategoryColor(category: Int): Int {
        var color = Color.GREEN
        when (category) {
            0 -> color = Color.CYAN
            1 -> color = Color.GREEN
            2 -> color = Color.MAGENTA
            3 -> color = Color.RED
            4 -> color = Color.YELLOW
        }
        return color
    }
    private fun createDrawableFroview(view: View): Bitmap {
        val displayMatrics = DisplayMetrics()
        (con as Activity).windowManager.defaultDisplay.getMetrics(displayMatrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.measure(displayMatrics.widthPixels, displayMatrics.heightPixels)
        view.layout(0, 0, displayMatrics.widthPixels, displayMatrics.heightPixels)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
