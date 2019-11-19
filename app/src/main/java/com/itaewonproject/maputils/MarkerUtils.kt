package com.itaewonproject.maputils

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.mypage.WishlistMapFragment
import com.itaewonproject.search.LocationActivity

class MarkerUtils(val map: GoogleMap, private val con: Context) {
    val view: View
    val image: ImageView
    private val latLoc = HashMap<LatLng, Location>()
    private var selectedMarker: Marker?
    var isWishlist=false
    var fragment:Fragment?=null
    private val imageList = listOf(listOf(
        R.drawable.ic_map_pin_fill_blue,
        R.drawable.ic_map_pin_fill_green,
        R.drawable.ic_map_pin_fill_purple,
        R.drawable.ic_map_pin_fill_red,
        R.drawable.ic_map_pin_fill_yellow
    ),
        listOf(
            R.drawable.ic_map_pin_not_fill_blue,
            R.drawable.ic_map_pin_not_fill_green,
            R.drawable.ic_map_pin_not_fill_purple,
            R.drawable.ic_map_pin_not_fill_red,
            R.drawable.ic_map_pin_not_fill_yellow
        ))

    init {
        view = LayoutInflater.from(con).inflate(R.layout.view_route_marker, null)
        image = view.findViewById(R.id.image) as ImageView
        selectedMarker = null
        map.setOnMarkerClickListener {
            map.moveCamera(CameraUpdateFactory.newLatLng(it.position))
            if (it != null && selectedMarker != null) {
                if (selectedMarker!!.position == it.position) {
                    if(isWishlist) (fragment as WishlistMapFragment).showDetail(null)
                    else (con as LocationActivity).showArticleActivity(latLoc[selectedMarker!!.position]!!)
                    changeSelectedMarker(null)
                    selectedMarker!!.remove()
                    selectedMarker =null
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
        val categoryColor = CategoryIcon.getIndex(location.category!!)
        image.setImageResource(if(isSelected) imageList[0][categoryColor] else imageList[1][categoryColor])
        val position = location.latlng()
        val markerOptions = MarkerOptions()
        markerOptions.title(location.name)
        markerOptions.position(position)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFroview(view)))
        val marker = map.addMarker(markerOptions)
        marker.showInfoWindow()
        if(isWishlist && isSelected) (fragment as WishlistMapFragment).showDetail(location)
        latLoc[position] = location
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


    private fun addLocationMarker(marker: Marker, isSelected: Boolean): Marker? {
        if (latLoc.containsKey(marker.position)) {
            val location = latLoc[marker.position]
            if (location != null)
                return addLocationMarker(location, isSelected)
        }
        return null
    }

    private fun createDrawableFroview(view: View): Bitmap {
        val displayMatrics = DisplayMetrics()
        (con as Activity).windowManager.defaultDisplay.getMetrics(displayMatrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.measure(displayMatrics.widthPixels, displayMatrics.heightPixels)
        view.layout(0, 0, displayMatrics.widthPixels, displayMatrics.heightPixels)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
