package com.itaewonproject

import android.app.Application
import com.itaewonproject.model.receiver.Product
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.model.sender.Location


class Routepang: Application() {
    //var customerId:Long=0
    var token:String =""
    var customer = Customer()
    var userToSee = Customer()
    var wishlist = arrayListOf<Product>()
    fun hasProduct(location:Location):Product?{
        val id = location.placeId
        for(w in wishlist){
            if(w.location.placeId == id){
               return w
            }
        }
        return null
    }
}