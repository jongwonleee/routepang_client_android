package com.itaewonproject.A.API

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.itaewonproject.API
import com.itaewonproject.APIs
import com.itaewonproject.HttpClient
import com.itaewonproject.ServerModel.Article
import com.itaewonproject.ServerModel.Location

class API3: API<Article>() {
    override var param=""
    override var method: String = "POST"
    override var inner: String ="article/postArticle/"

    fun postByCustomerId(article:Article){
        param = "customer_id=${article.customerId}&&link_id=${article.link.linkId}"
        article.customerId=1
        var task = Task()
        task.execute(Gson().toJson(article))
        //task.get()
        /*var result = task.get()
        var arr= locationJsonParsing(result)
        var ret = ArrayList<com.itaewonproject.ServerResult.Location>()*/

    }

}