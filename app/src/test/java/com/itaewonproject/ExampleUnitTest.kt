package com.itaewonproject

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.model.receiver.Location
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test(){
        var num :Int =0
        for (i in 1..10) num += i
        val n = 55
        assertEquals(num,n)
    }

   /* @Test
    fun test2(){

        var str="{\n" +
                "   \"html_attributions\" : [],\n" +
                "   \"result\" : {\n" +
                "      \"name\" : \"Pl de Carles Buïgas\",\n" +
                "      \"rating\" : 4.3\n" +
                "   },\n" +
                "   \"status\" : \"OK\"\n" +
                "}"
        var str2=""
        try{
            str2 = WebParser.execute().get()

        }catch(e:InterruptedException){
            e.printStackTrace()
        }catch(e: ExecutionException){
            e.printStackTrace()
        }
        assertEquals(str,str2)

    }*/

    @Test
    fun jsonParsing(){
        var latitude = 12.3333333
        var longitude = 123.5555555
        val apiResult = """
            [
                {
                        "title": "title1",
                         "imgUrl": ["${APIs.bmp1}","${APIs.bmp2}","${APIs.bmp3}","${APIs.bmp4}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 3.5f,
                        "placeId": "1",
                        "latitude":${latitude+0.00},
                        "longitude":${longitude+0.01},
                        "category":0,
                        "articleCount":55,
                        "usedTime":3000
                        
                    },
                    {
                        "title": "title2",
                         "imgUrl": ["${APIs.bmp1}","${APIs.bmp2}","${APIs.bmp3}","${APIs.bmp4}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 5.0f,
                        "placeId": "2",
                        "latitude":${latitude+0.01},
                        "longitude":${longitude+0.00},
                        "category":1,
                        "articleCount":45,
                        "usedTime":7540
                        
                    },
                    {
                        "title": "title3",
                         "imgUrl":["${APIs.bmp4}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 2f,
                        "placeId": "3",
                        "latitude":${latitude-0.01},
                        "longitude":${longitude+0.0},
                        "category":2,
                        "articleCount":25,
                        "usedTime":3000
                        
                    },
                    {
                        "title": "title4",
                         "imgUrl": ["${APIs.bmp3}","${APIs.bmp1}"],
                        "rating": 4.5f,
                        "placeId": "4",
                        "latitude":${latitude+0.00},
                        "longitude":${longitude-0.01},
                        "category":3,
                        "articleCount":32,
                        "usedTime":3600
                        
                    },
                    {
                        "title": "title5",
                         "imgUrl":["${APIs.bmp3}","${APIs.bmp2}","${APIs.bmp5}","${APIs.bmp4}","${APIs.bmp3}","${APIs.bmp2}", "${APIs.bmp1}"],
                        "rating": 3.5f,
                        "placeId": "5",
                        "latitude":${latitude+0.02},
                        "longitude":${longitude+0.0},
                        "category":4,
                        "articleCount":98,
                        "usedTime":3230
                        
                }
            ]
        """.trimIndent()


        var arr1 = APIs.API1JsonParsing(longitude,latitude,apiResult)
        var arr2 = ArrayList<Location>()
        var gson = Gson()

        try{
            arr2.addAll(gson.fromJson<ArrayList<Location>>(apiResult, object : TypeToken<ArrayList<Location>>(){}.type))
        }catch(e: JsonParseException)
        {
            e.printStackTrace()
        }
        System.out.println("arr1: ${arr1}")
        System.out.println("arr2: ${arr2}")

        //assertArrayEquals(arr1,arr2)
        assertEquals(gson.toJson(arr1), gson.toJson(arr1))
    }

}
