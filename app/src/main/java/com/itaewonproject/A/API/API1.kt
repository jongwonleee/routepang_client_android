package com.itaewonproject.A.API

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.itaewonproject.API
import com.itaewonproject.APIs
import com.itaewonproject.HttpClient
import com.itaewonproject.ServerResult.Location

class API1: API<Location>() {
    override var param=""
    override var method: String = "GET"
    override var inner: String ="location/getLocationByCoordinate/"

    fun getByLatLng(coordinate: LatLng, zoom:Float):ArrayList<com.itaewonproject.ServerResult.Location>{
        param = "latitude=${coordinate.longitude}&&longitude=${coordinate.latitude}"

        var task = Task()
        task.execute()

        var result = task.get()
        var arr= locationJsonParsing(result)

        return arr
    }

}