package com.itaewonproject.rests.get

import com.itaewonproject.rests.WebResponce

class GetNewsfeedConnector : GetStrategy() {

    override val inner: String = "feed/"
    override var param = ""
    override val mockData=""

    override fun get(vararg params: Any): WebResponce {
        val id = params[0] as Long
        param=id.toString()

        val task = Task()
        task.execute()

        return WebResponce(task.get(), statusCode)
    }

}
