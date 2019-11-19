package com.itaewonproject.rests.get

import com.itaewonproject.rests.WebResponce

class GetCustomerPageConnector : GetStrategy() {

    override val inner: String = "customer/"
    override var param = ""
    override val mockData: String = """
[
]
        """.trimIndent()

    override fun get(vararg params: Any): WebResponce {
        val id = params[0] as Long
        param = "$id/customerpage"

        val task = Task()
        task.execute()

        return WebResponce(task.get(), statusCode)
    }
}
