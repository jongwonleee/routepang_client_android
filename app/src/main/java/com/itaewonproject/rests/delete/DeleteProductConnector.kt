package com.itaewonproject.rests.delete

import com.google.gson.Gson
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.WebResponce

class DeleteProductConnector : DeleteStrategy() {

    override val inner = "product/"
    override var param = ""
    override val mockData: String = "[]"

    override fun delete(vararg params: Any): WebResponce {
        val product = params[0] as Product
        val id = params[1] as Long
        val task = Task()
        param = "$id/customers"
        task.execute(Gson().toJson(product))
        return WebResponce(task.get(), statusCode)
    }
}
