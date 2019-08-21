package com.itaewonproject.ServerResult

import android.graphics.Bitmap
import android.view.ViewDebug
import com.google.android.gms.maps.model.LatLng
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.linearref.LinearGeometryBuilder
import java.io.Serializable

/*
 category:
 0 - 숙소
 1 - 관광
 2 - 맛집
 3 - 쇼핑
 4 - 액티비티
 */
class Location:Serializable{
    var name=""
    var imgUrl: ArrayList<String>
    var rating=0f
    var placeId=""
    var latitude=0.0
    var longitude=0.0
    var category =0
    var articleCount=0
    var usedTime=0
    var locationId:Long=0
    var address=""
    constructor(location:com.itaewonproject.ServerModel.Location){
        imgUrl=ArrayList()
        name=location.name
        address=location.address
        latitude = location.coordinates.x
        longitude = location.coordinates.y
        locationId = location.locationId
        placeId = location.placeId
    }
    fun getServerModel():com.itaewonproject.ServerModel.Location{
        var ret = com.itaewonproject.ServerModel.Location()
        ret.name = name
        ret.address = address
        ret.coordinates = getPoint()
        ret.locationId=locationId
        ret.placeId=placeId
        return ret
    }
    constructor(name:String, urls:ArrayList<String>, rating:Float, placeId:String,latitude:Double,longitude:Double,usedTime:Int,category: Int,articleCount:Int){
        this.name=name
        this.imgUrl=urls
        this.rating=rating
        this.placeId=placeId
        this.latitude=latitude
        this.longitude=longitude
        this.usedTime=usedTime
        this.category=category
        this.articleCount=articleCount
    }

    fun latlng():LatLng{return LatLng(latitude,longitude)}
    fun getPoint(): Point {
        return GeometryFactory().createPoint(Coordinate(latitude,longitude))
    }
}