package com.itaewonproject.player

import com.google.gson.Gson
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.JsonParser
import com.itaewonproject.model.sender.Link

class LinkConnector: WebConnectStrategy() {
    override var param=""
    override var method: String = "POST"
    override var inner: String ="link/postLink"

    fun postByLink(url:String): Link {
        param = ""

        var task = Task()
        task.execute("[${Gson().toJson(url)}]")

        var result = task.get()
        return JsonParser().objectJsonParsing(result,Link::class.java)!!
    }


}