package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.receiver.ChatMessage
import com.itaewonproject.model.sender.Follow
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.WebResponce

class PostChatConnector : PostStrategy() {

    override val inner = "chat/send"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val chat = params[0] as ChatMessage
        val task = Task()
        task.execute(Gson().toJson(chat))
        return WebResponce(task.get(), statusCode)
    }
}
