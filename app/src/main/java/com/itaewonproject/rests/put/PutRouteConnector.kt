package com.itaewonproject.rests.put

import com.google.gson.Gson
import com.itaewonproject.model.sender.Product
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.rests.PostStrategy
import com.itaewonproject.rests.PutStrategy
import com.itaewonproject.rests.WebResponce

class PutRouteConnector : PutStrategy() {

    override val inner = "product/"
    override var param = ""
    override val mockData: String = "[]"

    override fun put(vararg params: Any): WebResponce {
        val routes = params[0] as Product
        val id = params[1] as Long
        var task = Task()
        param = "$id/routes"
        task.execute(Gson().toJson(routes))
        return WebResponce(task.get(), statusCode)
    }
}
