package com.itaewonproject.rests.get

import com.itaewonproject.rests.WebResponce

class GetChatConnector : GetStrategy() {

    override val inner: String = "chat/read"
    override var param = ""
    override val mockData=""

    override fun get(vararg params: Any): WebResponce {
        val senderId = params[0] as Long
        val receiverId = params[1] as Long
        contents.add(Pair("receiverId",receiverId.toString()))
        contents.add(Pair("senderId",senderId.toString()))
        val task = Task()
        task.execute()

        return WebResponce(task.get(), statusCode)
    }

}
