package com.itaewonproject.A.API

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.itaewonproject.API
import com.itaewonproject.APIs
import com.itaewonproject.HttpClient
import com.itaewonproject.ServerResult.Article

class API2: API<Article>() {
    override var param=""
    override var method: String = "GET"
    override var inner: String ="article/getArticleByLocationId/"

    fun getByLocationId(id:Long):ArrayList<Article>{
        param = "$id"

        var task = Task()
        task.execute()

        var result = task.get()


        return articleJsonParsing(result)
    }


}