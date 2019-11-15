package com.itaewonproject.model.receiver

import kotlin.collections.ArrayList

import java.sql.Timestamp

class Folder(override var title: String, override var boundary: String, override var routeId: Long, var routes: ArrayList<Route>, override var customerId: Long,override var regDate: Timestamp) : RouteListBase {
    override var type = RouteType.FOLDER
    override var parentId = -1
    override var endDate: Timestamp
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var startDate: Timestamp
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    var opened = false
    init {
        //regDate = getDate(time)
    }

    override fun equals(other: Any?): Boolean {
        return other is Folder && routeId == other.routeId
    }
    fun calculateDate() {
        endDate = routes[0].regDate
        startDate = routes[0].regDate
        for (r in routes) {
            if (endDate.after(r.regDate)) endDate = r.regDate
            if (startDate.before(r.regDate)) startDate = r.regDate
        }
    }
}
