package com.itaewonproject.rests.get

import com.itaewonproject.rests.WebResponce

class GetFollowingConnector : GetStrategy() {

    override val inner: String = "follow/following/list"
    override var param = ""
    override val mockData=""

    override fun get(vararg params: Any): WebResponce {
        val id = params[0] as Long
        val task = Task()
        contents.add(Pair("customerId",id.toString()))
        task.execute()

        return WebResponce(task.get(), statusCode)
    }

}
