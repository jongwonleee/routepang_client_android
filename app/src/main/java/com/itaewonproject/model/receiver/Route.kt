package com.itaewonproject.model.receiver

import java.sql.Timestamp

class Route(override var title: String, override var boundary: String, override var routeId: Long, override var regDate: Timestamp,
            override var customerId: Long) : RouteListBase {
    override var type = RouteType.ROUTE
    override var parentId = -1
    override var endDate: Timestamp
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var startDate: Timestamp
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    init {
        //regDate = getDate(time)
    }

    override fun equals(other: Any?): Boolean {
        return other is Route && routeId == other.routeId
    }
}
