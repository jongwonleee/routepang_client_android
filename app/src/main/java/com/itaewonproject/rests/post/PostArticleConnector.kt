package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.APIs
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.sender.Link
import com.itaewonproject.model.sender.Location
import com.itaewonproject.rests.PostStrategy
import com.itaewonproject.rests.WebResponce

class PostArticleConnector : PostStrategy() {

    override var param = ""
    override var inner: String = "customer/postArticle/"
    override lateinit var mockData: String
    init {
        mockData = "[]"
    }

    override fun post(vararg params: Any): WebResponce {
        val link = params[0] as Link
        val article = params[1] as Article
        val location = params[2] as Location
        param = "customerId=${article.customerId}&&linkId=${article.link.linkId}"
        article.customerId = 1
        var task = Task()
        task.execute(Gson().toJson(article))

        return WebResponce(task.get(), statusCode)
    }
}
