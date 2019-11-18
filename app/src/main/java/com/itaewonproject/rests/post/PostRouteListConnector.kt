package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.rests.PostStrategy
import com.itaewonproject.rests.WebResponce

class PostRouteListConnector : PostStrategy() {

    override val inner = "route/"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val routes = params[0] as ArrayList<Route>
        val id = params[1] as Long
        var task = Task()
        param = "$id/customers"
        task.execute(Gson().toJson(routes))
        return WebResponce(task.get(), statusCode)
    }
}
