package com.itaewonproject.player

import android.util.Log
import com.itaewonproject.JsonParser
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.model.receiver.Article

class ReviewConnector: WebConnectStrategy() {
    override var method: String = "GET"
    override var inner: String ="article/getArticleByCustomerId/"
    override var param=""

    fun getByCustomerId(id:Long):ArrayList<Article>{
        param = "$id"

        var task = Task()
        task.execute()

        Log.i("!@!","$statusCode")
        var result = task.get()


        return JsonParser().listJsonParsing(result,Article::class.java)//articleJsonParsing(result)
    }


}