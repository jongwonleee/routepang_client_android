package com.itaewonproject

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.model.receiver.Location
import org.junit.Test

import org.junit.Assert.*
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.CoordinateSequence
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

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

    @Test
    fun test3(){
        var c = GeometryFactory().createPoint()
        c.coordinate
        System.out.println(Gson().toJson(c))

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


}
