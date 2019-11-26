package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.sender.Follow
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.WebResponce

class PostFollowConnector : PostStrategy() {

    override val inner = "follow/"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val product = params[0] as Follow
        val task = Task()
        task.execute(Gson().toJson(product))
        return WebResponce(task.get(), statusCode)
    }
}
