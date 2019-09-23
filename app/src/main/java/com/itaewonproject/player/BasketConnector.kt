package com.itaewonproject.player

import com.google.gson.Gson
import com.itaewonproject.APIs
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.model.receiver.Location

class BasketConnector : WebConnectStrategy() {

    override var method: String = "GET"
    override var inner: String = "customer/getBasketListByCustomerId/"
    override var param = ""
    override lateinit var mockData: String
    init {
        val latitude: Double = 37.576110
        val longitude: Double = 126.976819
        mockData = """
            [
                {
                        "name": "title1",
                         "imgUrl": ["${APIs.bmp1}","${APIs.bmp2}","${APIs.bmp3}","${APIs.bmp4}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 3.5f,
                        "placeId": "1",
                        "latitude":${latitude + 0.00},
                        "longitude":${longitude + 0.01},
                        "category":0,
                        "articleCount":55,
                        "usedTime":3000
                        
                    },
                    {
                        "name": "title2",
                         "imgUrl": ["${APIs.bmp1}","${APIs.bmp2}","${APIs.bmp3}","${APIs.bmp4}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 5.0f,
                        "placeId": "2",
                        "latitude":${latitude + 0.01},
                        "longitude":${longitude + 0.00},
                        "category":1,
                        "articleCount":45,
                        "usedTime":7540
                        
                    },
                    {
                        "name": "title3",
                         "imgUrl":["${APIs.bmp4}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 2f,
                        "placeId": "3",
                        "latitude":${latitude - 0.01},
                        "longitude":${longitude + 0.0},
                        "category":2,
                        "articleCount":25,
                        "usedTime":3000
                        
                    },
                    {
                        "name": "title4",
                         "imgUrl": ["${APIs.bmp3}","${APIs.bmp1}"],
                        "rating": 4.5f,
                        "placeId": "4",
                        "latitude":${latitude + 0.00},
                        "longitude":${longitude - 0.01},
                        "category":3,
                        "articleCount":32,
                        "usedTime":3600
                        
                    },
                    {
                        "name": "title5",
                         "imgUrl":["${APIs.bmp3}","${APIs.bmp2}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 3.5f,
                        "placeId": "5",
                        "latitude":${latitude + 0.02},
                        "longitude":${longitude + 0.0},
                        "category":4,
                        "articleCount":98,
                        "usedTime":3230
                        
                }
            ]
        """.trimIndent()
    }
    override fun get(vararg params: Any): String {
        val id = params[0] as Long
        method = "GET"
        inner = "customer/getBasketListByCustomerId/"
        param = "customerId=$id"

        var task = Task()
        task.execute()

        return task.get()
    } /*

    fun getByCustomerId(id:Long):ArrayList<Location>{
        method="GET"
        inner="customer/getBasketListByCustomerId/"
        param = "$id"

        var task = Task()
        task.execute()

        var result = task.get()


        return JsonParser().listJsonParsing(result,Location::class.java)
    }
*/
    fun addBasketByLocation(id: Long, location: Location) {
        method = "POST"
        inner = "customer/addBasket/"
        param = "customerId=$id"
        var task = Task()
        task.execute(Gson().toJson(location.getServerModel()))
        task.get()
    }
}
