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

class API1: API<Article>() {
    override var method: String = "GET"
    override var inner: String ="article/getArticleByCustomerId/"
    override var param=""

    fun getByCustomerId(id:Long):ArrayList<Article>{
        param = "$id"

        var task = Task()
        task.execute()

        Log.i("!@!","$statusCode")
        var result = task.get()


        return articleJsonParsing(result)
    }


}