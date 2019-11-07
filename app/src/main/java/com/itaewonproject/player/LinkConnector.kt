package com.itaewonproject.player

import android.util.Log
import com.google.gson.Gson
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.linkshare.LinkManager
import com.itaewonproject.model.sender.Link

class LinkConnector : WebConnectStrategy() {

    override var param = ""
    override var method: String = "POST"
    override var inner: String = "link/postLink"
    override lateinit var mockData: String
    init {
        offlineMock("https://www.instagram.com/p/BuEAfrTDuvRGi-rjsieqDz_s0mWVZYnaoHiJyY0/")
    }
    override fun get(vararg params: Any): String {
        param = ""
        val url = params[0] as String
        if (isOffline) {
            offlineMock(url)
        }
        var task = Task()
        task.execute("[${Gson().toJson(url)}]")

        return task.get()
    }
    /*
    fun postByLink(url:String): Link {
        param = ""
        if(isOffline){
            offlineMock(url)
        }
        var task = Task()
        task.execute("[${Gson().toJson(url)}]")

        var result = task.get()
        return JsonParser().objectJsonParsing(result,Link::class.java)!!
    }*/

    private fun offlineMock(url: String) {
        var link = Link()
        link.linkUrl = url
        link = LinkManager().LinkApi(url)

        mockData = Gson().toJson(link)
        Log.i("link json", mockData)
    }
}
