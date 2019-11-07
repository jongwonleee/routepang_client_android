package com.itaewonproject.player

import com.google.android.gms.maps.model.LatLng
import com.itaewonproject.APIs
import com.itaewonproject.WebConnectStrategy

class LocationConnector : WebConnectStrategy() {

    /*override fun getResult(params: List<Any>): Any {
        val coordinate= params[0] as LatLng
        val zoom = params[1] as Float

        param = "latitude=${coordinate.longitude}&&longitude=${coordinate.latitude}"
        var task = Task()
        task.execute()

        var result = task.get()
        var ret = JsonParser<Location>().locationJsonParsing(result)

        return ret
    }*/

    override var param = ""
    override var method: String = "GET"
    override var inner: String = "location/getLocationByCoordinate/"
    override var mockData: String = """
[
    {
        "locationId": 2,
        "coordinates": "POINT (41.374405 2.16957)",
        "placeId": "ChIJVaNodVyipBIRvUHig6zNZkQ",
        "address": "Carrer Nou de la Rambla, 113, 08004 Barcelona, 스페인",
        "name": "Sala Apolo",
        "used": 1000,
        "category": "ATTRACTION",
        "articleCount": 0,
        "imgUrl": [
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q"
        ]
    },
    {
        "locationId": 3,
        "coordinates": "POINT (41.374812 2.168791)",
        "imgUrl":["https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q"],
        "placeId": "ChIJOfdJDFyipBIRYkepJGzr6FQ",
        "address": "Av. del Paraŀlel, 67, 08004 Barcelona, 스페인",
        "name": "Teatre Victoria",
        "used": 300,
        "category": "FOOD",
        "articleCount": 0
    },
    {
        "locationId": 4,
        "coordinates": "POINT (41.375203 2.169433)",
        "placeId": "ChIJIWQECFyipBIR7lEDCqHMde4",
        "address": "Av. del Paraŀlel, 62, 08001 Barcelona, 스페인",
        "name": "BARTS, Barcelona Arts on Stage",
        "used": 3000,
        "category": "FOOD",
        "articleCount": 0
    },
    {
        "locationId": 5,
        "coordinates": "POINT (41.375174 2.168579)",
        "placeId": "ChIJsajtF1yipBIRnk1NDxGtvFc",
        "address": "Av. del Paraŀlel, 76, 08001 Barcelona, 스페인",
        "name": "Nagano",
        "used": 200,
        "category": "ACTIVITY",
        "articleCount": 0
    },
    {
        "locationId": 6,
        "coordinates": "POINT (41.37516 2.170794)",
        "placeId": "ChIJr8howluipBIRrkGQRDkSQ5E",
        "address": "Av. del Paraŀlel, 54, 08004 Barcelona, 스페인",
        "name": "Cafe 365",
        "used": 2000,
        "category": "ACTIVITY",
        "articleCount": 0
    },
    {
        "locationId": 7,
        "coordinates": "POINT (41.375176 2.170633)",
        "placeId": "ChIJdZ1u6FuipBIRT-B8FRhSxH4",
        "address": "Carrer Nou de la Rambla, 105, 08001 Barcelona, 스페인",
        "name": "El Rincon del Artista",
        "used": 500,
        "category": "ACTIVITY",
        "articleCount": 0
    },
    {
        "locationId": 8,
        "coordinates": "POINT (41.374135 2.169411)",
        "placeId": "ChIJOYjNd1yipBIRvEr_Xu8tawY",
        "address": "Carrer de Vila i Vilà, 60, 08004 Barcelona, 스페인",
        "name": "Marcopolo‘s",
        "used": 600,
        "category": "FOOD",
        "articleCount": 0
    },
    {
        "locationId": 9,
        "coordinates": "POINT (41.374192 2.16858)",
        "placeId": "ChIJ7yEvQ1yipBIRSIytcqIN3Vo",
        "address": "Carrer de Vila i Vilà, 77, 08004 Barcelona, 스페인",
        "name": "Abirradero - CraftBeer - Tapas",
        "used": 3000,
        "category": "FOOD",
        "articleCount": 0
    },
    {
        "locationId": 10,
        "coordinates": "POINT (41.374509 2.167236)",
        "placeId": "ChIJrVaISlyipBIRmaaOY_tcqtU",
        "address": "Carrer de Vila i Vilà, 99, 08004 Barcelona, 스페인",
        "name": "El Molino Paral-lel",
        "used": 5000,
        "category": "ATTRACTION",
        "articleCount": 0
    }
]
        """.trimIndent()
    init {
    }
    override fun get(vararg params: Any): String {
        val coordinate = params[0] as LatLng
        val zoom = params[1] as Float

        param = "latitude=${coordinate.longitude}&&longitude=${coordinate.latitude}"
        var task = Task()
        task.execute()

        return task.get()
    }
    /*fun getByLatLng(coordinate: LatLng, zoom:Float):ArrayList<Location>{
        param = "latitude=${coordinate.longitude}&&longitude=${coordinate.latitude}"
        var task = Task()
        task.execute()

        var result = task.get()
        var arr= JsonParser().listJsonParsing(result,Location::class.java)

        return arr
    }*/
}
