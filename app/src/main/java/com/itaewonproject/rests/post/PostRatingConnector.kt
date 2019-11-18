package com.itaewonproject.rests.post

import com.google.gson.Gson
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Rating
import com.itaewonproject.rests.PostStrategy
import com.itaewonproject.rests.WebResponce

class PostRatingConnector : PostStrategy() {

    override val inner = "rating/location"
    override var param = ""
    override val mockData: String = "[]"

    override fun post(vararg params: Any): WebResponce {
        val rating = params[0] as Rating
        var task = Task()
        task.execute(Gson().toJson(rating))
        return WebResponce(task.get(), statusCode)
    }
}
