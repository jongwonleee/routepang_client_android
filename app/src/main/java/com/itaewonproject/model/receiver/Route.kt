package com.itaewonproject.model.receiver



class Route(override var title: String, override var boundary: String, override var routeId: Long, override var regDate: Long,
            override var customerId: Long) : RouteListBase {
    override var type = RouteType.ROUTE
    override var parentId = -1
    override var endDate: Long=0
    override var startDate: Long=0
    init {
        //regDate = getDate(time)
    }

    override fun equals(other: Any?): Boolean {
        return other is Route && routeId == other.routeId
    }
}
