package com.itaewonproject.model.receiver

import kotlin.collections.ArrayList

import java.sql.Timestamp
import java.text.SimpleDateFormat

class Route(var title: String,  var boundary: String,  var routeId: Long, var customerId: Long, var regDate: Long) {
    var type = RouteType.FOLDER
    var parentId = 0
    var routes: ArrayList<Route> = arrayListOf()
    var endDate: Long=0
    var startDate: Long=0
    var opened = false

    constructor(title: String, boundary: String, routeId: Long,routes:ArrayList<Route>,customerId: Long,regDate: Long) : this(title,boundary, routeId, customerId, regDate){
        this.routes = routes
    }


    override fun equals(other: Any?): Boolean {
        return routeId == (other as Route).routeId
    }
    fun calculateDate() {
        endDate = routes[0].regDate
        startDate = routes[0].regDate
        for (r in routes) {
            if (Timestamp(endDate).after(Timestamp(r.regDate))) endDate = r.regDate
            if (Timestamp(startDate).before(Timestamp(r.regDate))) startDate = r.regDate
        }
    }
    fun getDate(time: Long): String {
        return SimpleDateFormat("yyyy-mm-dd").format(Timestamp(time))
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