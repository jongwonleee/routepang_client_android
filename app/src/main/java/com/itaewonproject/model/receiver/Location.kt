package com.itaewonproject.model.receiver

import com.google.android.gms.maps.model.LatLng
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
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
    var imgUrl= arrayListOf<String>()
    var rating=0f
    var placeId=""
    var latitude=0.0
    var longitude=0.0
    var cate =0
    var category = ""
    var articleCount=0
    var used=0.0
    var locationId:Long=0
    var address=""
    constructor()
    constructor(location: com.itaewonproject.model.sender.Location){
        imgUrl=ArrayList()
        name=location.name
        address=location.address
        latitude = location.latitude
        longitude = location.longitude
        locationId = location.locationId
        placeId = location.placeId
    }
    fun getServerModel(): com.itaewonproject.model.sender.Location{
        var ret = com.itaewonproject.model.sender.Location()
        ret.name = name
        ret.address = address
        ret.articleCount=articleCount
        ret.locationId=locationId
        ret.placeId=placeId
        ret.category="FOOD"
        ret.used=used
        ret.latitude=latitude
        ret.longitude=longitude
        return ret
    }
    /*constructor(name:String, urls:ArrayList<String>, rating:Float, placeId:String,latitude:Double,longitude:Double,usedTime:Int,category: Int,articleCount:Int){
        this.name=name
        this.imgUrl=urls
        this.rating=rating
        this.placeId=placeId
        this.latitude=latitude
        this.longitude=longitude
        this.used=usedTime
        this.category=category
        this.articleCount=articleCount
    }
*/
    inline fun latlng()= LatLng(longitude,latitude)

    override fun hashCode(): Int {
        return locationId.toInt()
    }
}