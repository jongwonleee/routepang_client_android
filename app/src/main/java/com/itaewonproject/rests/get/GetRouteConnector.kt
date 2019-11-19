package com.itaewonproject.rests.get

import com.itaewonproject.APIs
import com.itaewonproject.model.sender.Link
import com.itaewonproject.rests.GetStrategy
import com.itaewonproject.rests.WebResponce

class GetRouteConnector : GetStrategy() {


    override var param = ""
    override val inner: String = "route/locations/"
    override lateinit var mockData: String
    init {
        mockData = "[]"

        }

    override fun get(vararg params: Any): WebResponce {
        val id = params[0] as Long
        param = "$id/routes"

        var task = Task()
        task.execute()

        return WebResponce(task.get(), statusCode)
    }

}

