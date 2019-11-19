package com.itaewonproject.rests.get

import com.itaewonproject.rests.WebResponce

class GetRouteListConnector : GetStrategy() {


    override var param = ""
    override val inner: String = "route/"
    override lateinit var mockData: String
    init {
        mockData = """[
    {
        "routeId": 1,
        "customerId": 1,
        "type": "FOLDER",
        "title": "바르셀로나 여행",
        "boundary": "Barcelona",
        "regDate": 1565937752000,
        "startDate": 1565937752000,
        "endDate": 1566283352000,
        "parentId": 0,
        "routes": [
            {
                "routeId": 2,
                "customerId": 1,
                "type": "ROUTE",
                "title": "사그라다 파밀리",
                "boundary": "Barcelona",
                "regDate": 1565937752000,
                "startDate": 1565937752000,
                "endDate": 1566024152000,
                "parentId": 1
            },
            {
                "routeId": 3,
                "customerId" : 1,
                "type": "ROUTE",
                "title": "구엘공원",
                "boundary": "Barcelona",
                "regDate": 1565937752000,
                "startDate": 1566024152000,
                "endDate": 1566110552000,
                "parentId": 1
            },
            {
                "routeId": 4,
                "customerId": 1,
                "type": "ROUTE",
                "title": "보케리아 도시",
                "boundary": "Barcelona",
                "regDate": 1565937752000,
                "startDate": 1566110552000,
                "endDate": 1566196952000,
                "parentId": 1
            },
            {
                "routeId": 5,
                "customerId": 1,
                "type": "ROUTE",
                "title": "몬세랏",
                "boundary": "Barcelona",
                "regDate": 1565937752000,
                "startDate": 1566196952000,
                "endDate": 1566283352000,
                "parentId": 1
            }
        ]
    },
    {
        "routeId": 6,
        "customerId": 1,
        "type": "ROUTE",
        "title": "오설록",
        "boundary": "Jeju",
        "regDate": 1565937752000,
        "startDate": 1566283352000,
        "endDate": 1566283352000,
        "parentId": 0
    },
    {
        "routeId": 7,
        "customerId": 1,
        "type": "ROUTE",
        "title": "아남타워",
        "boundary": "Seoul",
        "regDate": 1565937752000,
        "startDate": 1566369752000,
        "endDate": 1566369752000,
        "parentId": 0
    }
]
        """.trimIndent()

        }

    override fun get(vararg params: Any): WebResponce {
        val id = params[0] as Long
        param = "$id/customers"

        val task = Task()
        task.execute()

        return WebResponce(task.get(), statusCode)
    }

}

