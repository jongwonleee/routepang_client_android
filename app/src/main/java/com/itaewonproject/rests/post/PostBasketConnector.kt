package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.rests.WebResponce

class PostBasketConnector : PostStrategy() {

    override val inner = "product/"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val id = params[0] as Long
        val location = params[1] as Location
        param = "$id/customers"
        var task = Task()
        task.execute(Gson().toJson(location.getServerModel()))
        task.get()
        return WebResponce(task.get(), statusCode)
    }
}
