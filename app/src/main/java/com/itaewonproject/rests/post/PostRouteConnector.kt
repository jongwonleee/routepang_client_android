package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.model.sender.Follow
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.WebResponce

class PostRouteConnector : PostStrategy() {

    override val inner = "route/"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val id = params[0] as Long
        val route = params[1] as Route
        param = "$id/customers"
        val task = Task()
        task.execute(Gson().toJson(route))
        return WebResponce(task.get(), statusCode)
    }
}
