package com.itaewonproject.B_Mypage.API

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.API
import com.itaewonproject.APIs
import com.itaewonproject.HttpClient
import com.itaewonproject.ServerResult.Article
import com.itaewonproject.ServerResult.Location

class API2: API<Location>() {
    override var method: String = "GET"
    override var inner: String ="customer/getBasketListByCustomerId/"
    override var param=""

    fun getByCustomerId(id:Long):ArrayList<Location>{
        method="GET"
        inner="customer/getBasketListByCustomerId/"
        param = "$id"

        var task = Task()
        task.execute()

        var result = task.get()


        return locationJsonParsing(result)
    }

    fun addBasketByLocation(id:Long,location:Location){
        method="POST"
        inner="customer/addBasket/"
        param="$id"
        var task = Task()
        task.execute(Gson().toJson(location.getServerModel()))
        task.get()
    }


}