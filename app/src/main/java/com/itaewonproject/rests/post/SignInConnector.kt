package com.itaewonproject.rests.post


import com.google.gson.Gson
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.WebResponce

class SignInConnector : PostStrategy() {

    override var param = ""
    override val inner: String = "signup"
    override lateinit var mockData: String
    init {
        mockData = """
            []
        """.trimIndent()
    }

    override fun post(vararg params: Any): WebResponce{
        val customer = params[0] as Customer
        val task = Task()
        task.execute(Gson().toJson(customer))

        return WebResponce(task.get(), statusCode)

    }

}
