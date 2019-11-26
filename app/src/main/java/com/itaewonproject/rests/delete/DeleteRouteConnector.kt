package com.itaewonproject.rests.delete

import com.google.gson.Gson
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.WebResponce

class DeleteRouteConnector : DeleteStrategy() {

    override val inner = "route/"
    override var param = ""
    override val mockData: String = "[]"

    override fun delete(vararg params: Any): WebResponce {
        val id = params[0] as Long
        val task = Task()
        param = "$id"
        task.execute()
        return WebResponce(task.get(), statusCode)
    }
}
