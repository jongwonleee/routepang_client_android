package com.itaewonproject.rests.post

import android.util.Log
import com.google.gson.Gson
import com.itaewonproject.model.sender.Link
import com.itaewonproject.rests.PostStrategy
import com.itaewonproject.rests.WebResponce

class LinkConnector : PostStrategy() {

    override var param = ""
    override val inner: String = "link/postLink"
    override lateinit var mockData: String
    init {
        offlineMock("https://www.instagram.com/p/BuEAfrTDuvRGi-rjsieqDz_s0mWVZYnaoHiJyY0/")
    }
    override fun post(vararg params: Any): WebResponce {
        param = ""
        val url = params[0] as String
        if (isOffline) {
            offlineMock(url)
        }
        var task = Task()
        task.execute("[${Gson().toJson(url)}]")

        return WebResponce(task.get(), statusCode)
    }

    private fun offlineMock(url: String) {
        var link = Link()
        link.linkUrl = url
        //link = LinkManager().LinkApi(url)

        mockData = Gson().toJson(link)
        Log.i("link json", mockData)
    }
}
