package com.itaewonproject.model.receiver

import kotlin.collections.ArrayList

import java.sql.Timestamp

class Folder(override var title: String, override var boundary: String, override var routeId: Long,override var customerId: Long,override var regDate: Long) : RouteListBase {
    override var type = RouteType.FOLDER
    override var parentId = -1
    var routes: ArrayList<Folder> = arrayListOf()
    override var endDate: Long=0

    override var startDate: Long=0

    var opened = false

    constructor(title: String, boundary: String, routeId: Long,routes:ArrayList<Folder>,customerId: Long,regDate: Long) : this(title,boundary, routeId, customerId, regDate){
        this.routes = routes
    }


        override fun equals(other: Any?): Boolean {
        return other is Folder && routeId == other.routeId
    }
    fun calculateDate() {
        endDate = routes[0].regDate
        startDate = routes[0].regDate
        for (r in routes) {
            if (Timestamp(endDate).after(Timestamp(r.regDate))) endDate = r.regDate
            if (Timestamp(startDate).before(Timestamp(r.regDate))) startDate = r.regDate
        }
    }
}
