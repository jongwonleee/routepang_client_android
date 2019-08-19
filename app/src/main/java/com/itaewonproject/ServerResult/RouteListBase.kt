package com.itaewonproject.ServerResult

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

interface RouteListBase{
    var title:String
    var location:String
    var date: String
    var id:Int
    var type:Int
    var parIndex:Int
    fun getDate(time:Timestamp):String{
        return SimpleDateFormat("yyyy-mm-dd").format(time)
    }
}