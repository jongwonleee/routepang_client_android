package com.itaewonproject.A.API

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.itaewonproject.API
import com.itaewonproject.APIs
import com.itaewonproject.HttpClient
import com.itaewonproject.ServerModel.Link
import com.itaewonproject.ServerModel.Location

class API4: API<Location>() {
    override var param=""
    override var method: String = "POST"
    override var inner: String ="link/postLink"

    fun postByLink(url:String): Link {
        param = ""

        var task = Task()
        task.execute("[${Gson().toJson(url)}]")

        var result = task.get()
        return linkJsonParsing(result)
    }


}