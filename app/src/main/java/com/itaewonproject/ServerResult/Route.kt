package com.itaewonproject.ServerResult

import java.sql.Timestamp
import java.util.*

class Route(override var title:String, override var location:String,
            override var id: Int, var time:Timestamp):RouteListBase
{
    override var type=0
    override lateinit var date:String
    override var parIndex=-1
    init{
        date = getDate(time)
    }
}
