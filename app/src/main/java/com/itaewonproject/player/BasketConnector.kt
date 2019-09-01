package com.itaewonproject.player

import com.google.gson.Gson
import com.itaewonproject.JsonParser
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.model.receiver.Location

class BasketConnector: WebConnectStrategy() {
    override var method: String = "GET"
    override var inner: String ="customer/getBasketListByCustomerId/"
    override var param=""

    fun getByCustomerId(id:Long):ArrayList<Location>{
        method="GET"
        inner="customer/getBasketListByCustomerId/"
        param = "$id"

        var task = Task()
        task.execute()

        var result = task.get()


        return JsonParser().listJsonParsing(result,Location::class.java)
    }

    fun addBasketByLocation(id:Long,location:Location){
        method="POST"
        inner="customer/addBasket/"
        param="$id"
        var task = Task()
        task.execute(Gson().toJson(location.getServerModel()))
        task.get()
    }


}