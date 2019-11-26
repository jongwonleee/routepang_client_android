package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.receiver.ChatMessage
import com.itaewonproject.model.sender.Follow
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.WebResponce

class PostTokenConnector : PostStrategy() {

    override val inner = "notification/token"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val customerId = params[0] as Long
        val pushToken = params[1] as String
        contents.add(Pair("pushToken",pushToken))
        contents.add(Pair("customerId",customerId.toString()))
        val task = Task()

        task.execute()
        return WebResponce(task.get(), statusCode)
    }
}
