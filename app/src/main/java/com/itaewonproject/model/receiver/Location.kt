package com.itaewonproject.model.receiver

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.itaewonproject.maputils.LocationCategory
import com.itaewonproject.maputils.LocationCategoryParser
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
    public var name = ""
    public var imgUrl = arrayListOf<String>()
    public var rating = 0f
    public var placeId = ""
    public var coordinates =""
    public var category: LocationCategory? =null
    public var articleCount = 0
    public var usedTime = 0.0
    public var locationId: Long = 0
    public var address = ""
    constructor()
    constructor(location: com.itaewonproject.model.sender.Location) {
        imgUrl = ArrayList()
        name = location.name
        address = location.address
        Log.i("getting serverModel","${location.coordinates}")
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
    inline fun latlng() = LatLng(coordinates.substring(coordinates.indexOf('(')+1,coordinates.indexOf(' ',coordinates.indexOf('(')+1)-1).toDouble(),coordinates.substring(coordinates.indexOf(' ',coordinates.indexOf('(')+1)+1,coordinates.indexOf(')')-1).toDouble())

    override fun hashCode(): Int {
        return locationId.toInt()
    }
}
