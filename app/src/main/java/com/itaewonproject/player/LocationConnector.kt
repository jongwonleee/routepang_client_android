package com.itaewonproject.player

import com.google.android.gms.maps.model.LatLng
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.JsonParser
import com.itaewonproject.model.receiver.Location

class LocationConnector: WebConnectStrategy() {
    /*override fun getResult(params: List<Any>): Any {
        val coordinate= params[0] as LatLng
        val zoom = params[1] as Float

        param = "latitude=${coordinate.longitude}&&longitude=${coordinate.latitude}"
        var task = Task()
        task.execute()

        var result = task.get()
        var ret = JsonParser<Location>().locationJsonParsing(result)

        return ret
    }*/

    override var param=""
    override var method: String = "GET"
    override var inner: String ="location/getLocationByCoordinate/"
    fun getByLatLng(coordinate: LatLng, zoom:Float):ArrayList<Location>{
        param = "latitude=${coordinate.longitude}&&longitude=${coordinate.latitude}"
        var task = Task()
        task.execute()

        var result = task.get()
        var arr= JsonParser().listJsonParsing(result,Location::class.java)

        return arr
    }

}