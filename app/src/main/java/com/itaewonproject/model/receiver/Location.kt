package com.itaewonproject.model.receiver

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.itaewonproject.maputils.LocationCategory
import com.itaewonproject.model.sender.Location
import java.io.Serializable

/*
 category:
 0 - 숙소
 1 - 관광
 2 - 맛집
 3 - 쇼핑
 4 - 액티비티
 */
class Location : Serializable {
    var name = ""
    var imgUrl = arrayListOf<String>()
    var rating = 0f
    var placeId = ""
    var coordinates =""
    var category: LocationCategory? =null
    var articleCount = 0
    var usedTime = 0.0
    var locationId: Long = 0
    var address = ""
    constructor()
    constructor(location: Location) {
        imgUrl = ArrayList()
        name = location.name
        address = location.address
        Log.i("getting serverModel", location.coordinates)
        coordinates = location.coordinates
        locationId = location.locationId
        placeId = location.placeId
        category = location.category
    }
    fun getServerModel(): Location {
        val location = Location()
        location.placeId = placeId
        location.address = address
        location.name = name
        location.category =category
        location.coordinates = coordinates
        return location
    }
    final inline fun latlng() = LatLng(coordinates.substring(coordinates.indexOf('(')+1,coordinates.indexOf(' ',coordinates.indexOf('(')+1)-1).toDouble(),coordinates.substring(coordinates.indexOf(' ',coordinates.indexOf('(')+1)+1,coordinates.indexOf(')')-1).toDouble())

    override fun hashCode(): Int {
        return locationId.toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as com.itaewonproject.model.receiver.Location

        if (locationId != other.locationId) return false

        return true
    }
}
