package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.sender.Article
import com.itaewonproject.rests.WebResponce

class PostArticleConnector : PostStrategy() {

    override var param = ""
    override var inner: String = "article/"
    override lateinit var mockData: String
    init {
        mockData = "[]"
    }

    override fun post(vararg params: Any): WebResponce {
        val article = params[0] as Article
        param = ""

        val task = Task()
        task.execute(Gson().toJson(article))

        return WebResponce(task.get(), statusCode)
    }
}
