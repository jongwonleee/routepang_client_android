package com.itaewonproject.model.receiver

import kotlin.collections.ArrayList

import java.sql.Timestamp
import java.text.SimpleDateFormat

class Route(var title: String,  var boundary: String,  var routeId: Long, var customerId: Long, var regDate: Long) {
    var category = RouteType.ROUTE
    var parentId = 0
    var routes: ArrayList<Route> = arrayListOf()
    private var endDate: Long=0
    private var startDate: Long=0
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
    private fun getDate(time: Long): String {
        return SimpleDateFormat("yyyy-mm-dd").format(Timestamp(time))
    }
    fun getDateString():String{
        return if (endDate.equals(startDate)) {
            getDate(startDate)
        } else {
            getDate(startDate) + " ~\n " + getDate(endDate)
        }
    }

    override fun hashCode(): Int {
        var result = routeId.hashCode()
        return result
    }
}
enum class RouteType{ROUTE,FOLDER}