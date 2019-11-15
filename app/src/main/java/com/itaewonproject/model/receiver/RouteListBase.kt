package com.itaewonproject.model.receiver

import java.sql.Timestamp
import java.text.SimpleDateFormat

interface RouteListBase {
    var routeId: Long
    var customerId:Long
    var type: RouteType
    var title: String
    var boundary: String
    var regDate: Timestamp
    var startDate: Timestamp
    var endDate:Timestamp
    var parentId: Int
    fun getDate(time: Timestamp): String {
        return SimpleDateFormat("yyyy-mm-dd").format(time)
    }
    fun getDateString():String{
        if (endDate.equals(startDate)) {
           return getDate(startDate)
        } else {
            return getDate(startDate) + " ~\n " + getDate(endDate)
        }
    }
}

enum class RouteType{FOLDER,ROUTE}