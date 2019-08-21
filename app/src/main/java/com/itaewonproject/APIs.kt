package com.itaewonproject

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.ServerResult.Article
import com.itaewonproject.ServerResult.Location
import com.itaewonproject.ServerResult.Route
import java.io.IOException
import android.content.ContentValues
import android.os.Build
import com.itaewonproject.ServerResult.Folder
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.net.ssl.HttpsURLConnection


object APIs{
    val bmp1 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q"
    val bmp2 = "https://www.fcbarcelona.com/photo-resources/fcbarcelona/photo/2019/04/27/2b3900dc-06d6-417c-906e-477241e279a8/mini_2019-04-27-BARCELONA-LEVANTE-69.JPG?width=1200&height=750"
    val bmp3 = "https://us.123rf.com/450wm/pandavector/pandavector1705/pandavector170502056/77588042-stock-illustration-multicolored-inable-balls-party-and-parties-single-icon-in-cartoon-style-rater-bitmap-symbol-stock-i.jpg?ver=6"
    val bmp4 = "https://us.123rf.com/450wm/pandavector/pandavector1705/pandavector170501946/77588232-%EB%85%B8%EB%9E%80%EC%83%89-%EC%86%9C-%ED%84%B8-%EB%B3%91%EC%95%84%EB%A6%AC-%EB%B6%80%ED%99%9C%EC%A0%88-%EB%8B%A8%EC%9D%BC-%EC%95%84%EC%9D%B4%EC%BD%98-%EB%A7%8C%ED%99%94-%EC%8A%A4%ED%83%80%EC%9D%BC-rater-%EB%B9%84%ED%8A%B8-%EB%A7%B5-%EA%B8%B0%ED%98%B8-%EC%A3%BC%EC%8B%9D-%EA%B7%B8%EB%A6%BC%EC%9E%85%EB%8B%88%EB%8B%A4-.jpg?ver=6"
    val bmp5 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQRSot03kQt2dLn0RdPM_18910imxh5xydnfj8quawFqKl4El5WQ"

    fun API1(longitude:Double, latitude:Double, zoom:Float):ArrayList<Location>{

        var taskAPI1 = TaskAPI1()

        var hashmap = HashMap<String,String>()
        hashmap.put("longitude",longitude.toString())
        hashmap.put("latitude",latitude.toString())
        hashmap.put("zoom",zoom.toString())

        taskAPI1.execute(hashmap)



        val apiResult = """
            [
                {
                        "name": "title1",
                         "imgUrl": ["$bmp1","$bmp2","$bmp3","$bmp4","$bmp5","$bmp4","$bmp3","$bmp2", "$bmp1"],
                        "rating": 3.5f,
                        "placeId": "1",
                        "latitude":${latitude+0.00},
                        "longitude":${longitude+0.01},
                        "category":0,
                        "articleCount":55,
                        "usedTime":3000
                        
                    },
                    {
                        "name": "title2",
                         "imgUrl": ["$bmp1","$bmp2","$bmp3","$bmp4","$bmp5","$bmp4","$bmp3","$bmp2", "$bmp1"],
                        "rating": 5.0f,
                        "placeId": "2",
                        "latitude":${latitude+0.01},
                        "longitude":${longitude+0.00},
                        "category":1,
                        "articleCount":45,
                        "usedTime":7540
                        
                    },
                    {
                        "name": "title3",
                         "imgUrl":["$bmp4","$bmp5","$bmp4","$bmp3","$bmp2", "$bmp1"],
                        "rating": 2f,
                        "placeId": "3",
                        "latitude":${latitude-0.01},
                        "longitude":${longitude+0.0},
                        "category":2,
                        "articleCount":25,
                        "usedTime":3000
                        
                    },
                    {
                        "name": "title4",
                         "imgUrl": ["$bmp3","$bmp1"],
                        "rating": 4.5f,
                        "placeId": "4",
                        "latitude":${latitude+0.00},
                        "longitude":${longitude-0.01},
                        "category":3,
                        "articleCount":32,
                        "usedTime":3600
                        
                    },
                    {
                        "name": "title5",
                         "imgUrl":["$bmp3","$bmp2","$bmp5","$bmp4","$bmp3","$bmp2", "$bmp1"],
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


        return API1JsonParsing(longitude,latitude,apiResult)
    }

    fun B_API1(userID:Int):ArrayList<Folder>{
        var arr= ArrayList<Folder>()
        var in1 = Route("InRoute1","바르셀로나",101, Timestamp(1479249799770))
        var in2 = Route("InRoute2","바르셀로나",102,Timestamp(System.currentTimeMillis()))
        var in3 = Route("InRoute3","바르셀로나",103,Timestamp(System.currentTimeMillis()))
        var in4 = Route("InRoute4","바르셀로나",101,Timestamp(System.currentTimeMillis()))
        var r1 = Route("바르셀로나 여행기1","바르셀로나",1,Timestamp(1479249799770))
        var r2 = Route("바르셀로나 여행기2","바르셀로나",2,Timestamp(System.currentTimeMillis()))
        var r3 = Route("바르셀로나 여행기3","바르셀로나",3,Timestamp(System.currentTimeMillis()))
        var r4 = Route("바르셀로나 여행기4","바르셀로나",4,Timestamp(System.currentTimeMillis()))
        var r5 = Route("바르셀로나 여행기5","바르셀로나",5,Timestamp(System.currentTimeMillis()))
        var r6 = Route("바르셀로나 여행기6","바르셀로나",6,Timestamp(System.currentTimeMillis()))
        arr.add(Folder("바르셀로나 폴더1","바르셀로나",1,arrayListOf(in1,in2,in3,in4)))
        arr.add(Folder("바르셀로나 폴더2","바르셀로나",2,arrayListOf(in1,in2,in3)))
        arr.add(Folder("바르셀로나 폴더3","바르셀로나",3,arrayListOf(in1,in3,in4)))
        arr.add(Folder("","바르셀로나",4,arrayListOf(r1)))
        arr.add(Folder("","바르셀로나",5,arrayListOf(r2)))
        arr.add(Folder("","바르셀로나",6,arrayListOf(r3)))
        arr.add(Folder("","바르셀로나",7,arrayListOf(r4)))
        arr.add(Folder("","바르셀로나",8,arrayListOf(r5)))
        arr.add(Folder("","바르셀로나",9,arrayListOf(r6)))
        arr.add(Folder("바르셀로나 폴더4","바르셀로나",10,arrayListOf(in1,in2,in3,in4)))
        arr.add(Folder("바르셀로나 폴더5","바르셀로나",11,arrayListOf(in1,in2,in3)))
        arr.add(Folder("바르셀로나 폴더6","바르셀로나",12,arrayListOf(in1,in3,in4)))

        return arr
    }


    fun API1JsonParsing(longitude:Double, latitude:Double,result:String):ArrayList<Location>{
        var arr = ArrayList<Location>()
        var gson = Gson()
        try{
            arr.addAll(gson.fromJson<ArrayList<Location>>(result, object : TypeToken <ArrayList<Location>>(){}.type))
        }catch(e:JsonParseException)
        {
            e.printStackTrace()
        }/*
        for(i in 6..20){
            arr.add(
                Location(
                    "output$i",
                    arrayListOf<String>(bmp2, bmp4, bmp1, bmp4, bmp5, bmp1, bmp2, bmp4),
                    (i % 6) + 0f,
                    "$i",
                    latitude - 0.002,
                    longitude + 0.001 * i - 0.012,
                    i * 400,
                    i % 5,
                    (i * 24) % 49
                )
            )

        }
        System.out.println(Gson().toJson(arr))*/
        return arr
    }

    fun API2(placeID:String):ArrayList<com.itaewonproject.ServerModel.Article>{
       var ref = listOf<String>("https://facebookbrand.com/wp-content/themes/fb-branding/assets/images/fb-logo.png?v2",
            "https://instagram-brand.com/wp-content/uploads/2016/11/Instagram_AppIcon_Aug2017.png?w=300")
        var link = listOf<String>("http://www.facebook.com","http://www.instagram.com")

        var apiResult="""
            [
                {
                    "article_id":1,
                    "img_url":"$bmp1",
                    "link":"${link[1]}",
                    "ref_icon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle1"
                },
                {
                    "article_id":2,
                    "img_url":"$bmp2",
                    "link":"${link[0]}",
                    "ref_icon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle2"
                },
                {
                    "article_id":3,
                    "img_url":"$bmp3",
                    "link":"${link[1]}",
                    "ref_icon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle3"
                },
                {
                    "article_id":4,
                    "img_url":"$bmp4",
                    "link":"${link[0]}",
                    "ref_icon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle4"
                },
                {
                    "article_id":5,
                    "img_url":"$bmp5",
                    "link":"${link[1]}",
                    "ref_icon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle5"
                },
                {
                    "article_id":6,
                    "img_url":"$bmp1",
                    "link":"${link[0]}",
                    "ref_icon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle6"
                },
                {
                    "article_id":7,
                    "img_url":"$bmp2",
                    "link":"${link[1]}",
                    "ref_icon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle7"
                },
                {
                    "article_id":8,
                    "img_url":"$bmp3",
                    "link":"${link[0]}",
                    "ref_icon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle8"
                },
                {
                    "article_id":9,
                    "img_url":"$bmp4",
                    "link":"${link[1]}",
                    "ref_icon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle9"
                },
                {
                    "article_id":10,
                    "img_url":"$bmp5",
                    "link":"${link[0]}",
                    "ref_icon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle10"
                }
            ]
        """.trimIndent()

        var taskAPI2 = TaskAPI2()

        taskAPI2.execute()
        apiResult = taskAPI2.get()
        var arr = ArrayList<com.itaewonproject.ServerModel.Article>()
        var gson = Gson()
        try{
            arr.addAll(gson.fromJson<ArrayList<com.itaewonproject.ServerModel.Article>>(apiResult, object : TypeToken <ArrayList<com.itaewonproject.ServerModel.Article>>(){}.type))
        }catch(e:JsonParseException)
        {
            e.printStackTrace()
        }

        return arr
    }

    fun secToString(sec:Int):String {
        var ret = ""
        if(sec>=3600) ret+="${sec / 3600}시간"
        if (sec % 3600 != 0 || sec==0) ret += "${(sec % 3600) / 60}분"
        return ret
    }

    fun getCategoryImage(num:Int):Bitmap{
        var bmp = Bitmap.createBitmap(50,50,Bitmap.Config.ARGB_8888)
        when(num){
            0 -> bmp.eraseColor(Color.CYAN)
            1 -> bmp.eraseColor(Color.GREEN)
            2 -> bmp.eraseColor(Color.MAGENTA)
            3 -> bmp.eraseColor(Color.RED)
            4 -> bmp.eraseColor(Color.YELLOW)
        }
        return bmp
    }

    /*fun requestRouteDetail(routeId:Int):ArrayList<Location>{

    }*/


    class TaskAPI1: AsyncTask<Map<String, String>, Integer, String>() {
        val ip = "localhost"
        override fun doInBackground(vararg p0: Map<String, String>?): String {
            /*var url = URL( "http://ec2-52-78-225-101.ap-northeast-2.compute.amazonaws.com:9090/getLocationByCoordinate")
            var con = url.openConnection() as HttpsURLConnection
            con.requestMethod="GET"
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded")
            con.connectTimeout=5000
            con.readTimeout=5000
            con.doInput=true
            con.doOutput=true
            var os = con.outputStream
            var map:MutableMap<String,Point> = mutableMapOf(Pair("coordinates", GeometryFactory().createPoint(Coordinate(123.33333,23.44444444))))
            os.write(map.toString().toByteArray(charset("UTF-8")))
            os.flush()
            os.close()
            var responseCode = con.responseCode*/
            val http = HttpClient.Builder("GET", "http://ec2-52-78-225-101.ap-northeast-2.compute.amazonaws.com:9090/article/getArticleById/1") //포트번호,서블릿주소

            // Parameter 를 전송한다.
            //http.addAllParameters(mutableMapOf(Pair("coordinates", GeometryFactory().createPoint(Coordinate(123.33333,23.44444444)).toString())))

            Log.i("!!http Request URL",http.url)

            //Http 요청 전송
            val post = http.create()

            post.request()

            Log.i("!!postBody",post.body)


            // 응답 상태코드 가져오기
            val statusCode = post.httpStatusCode

            // 응답 본문 가져오기
            return post.body
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }

    class TaskAPI2: AsyncTask<Map<String, String>, Integer, String>() {
        val ip = "localhost"
        override fun doInBackground(vararg p0: Map<String, String>?): String {

            val http = HttpClient.Builder("GET", "http://ec2-52-78-225-101.ap-northeast-2.compute.amazonaws.com:9090/article/getArticleByLocationId/1") //포트번호,서블릿주소

            // Parameter 를 전송한다.
            //http.addAllParameters(mutableMapOf(Pair("coordinates", GeometryFactory().createPoint(Coordinate(123.33333,23.44444444)).toString())))

            Log.i("!!http Request URL",http.url)

            //Http 요청 전송
            val post = http.create()

            post.request()

            Log.i("!!postBody",post.body)


            // 응답 상태코드 가져오기
            val statusCode = post.httpStatusCode
            return post.body
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }

    fun getMarkerOption(context: Context, latLng: LatLng): MarkerOptions {
        var marker = MarkerOptions()
        var result=listOf<Address>()
        var place: Place
        marker.position(latLng)

        try{
            result = Geocoder(context).getFromLocation(latLng.latitude,latLng.longitude,1)
            if(result.size>0){
                marker.snippet(result[0].getAddressLine(0))
                marker.title(result[0].featureName)

            }
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e:IndexOutOfBoundsException){
            e.printStackTrace()
        }


        return marker
    }

 /*    class RequestHttpURLConnection {

        @TargetApi(Build.VERSION_CODES.N)
        fun request(_url: String, _params: ContentValues?): String? {

            // HttpURLConnection 참조 변수.
            var urlConn: HttpURLConnection? = null
            // URL 뒤에 붙여서 보낼 파라미터.
            val sbParams = StringBuffer()

            *//**
             * 1. StringBuffer에 파라미터 연결
             *//*
            // 보낼 데이터가 없으면 파라미터를 비운다.
            if (_params == null)
                sbParams.append("")
            else {
                // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
                var isAnd = false
                // 파라미터 키와 값.
                var key: String
                var value: String

                for ((key1, value1) in _params.valueSet()) {
                    key = key1
                    value = value1.toString()

                    // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                    if (isAnd)
                        sbParams.append("&")

                    sbParams.append(key).append("=").append(value)

                    // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                    if (!isAnd)
                        if (_params.size() >= 2)
                            isAnd = true
                }
            }// 보낼 데이터가 있으면 파라미터를 채운다.

            *//**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             *//*
            try {
                val url = URL(_url)
                urlConn = url.openConnection() as HttpURLConnection

                // [2-1]. urlConn 설정.
                //urlConn!!.setRequestMethod("POST") // URL 요청에 대한 메소드 설정 : POST.
                //urlConn!!.setRequestProperty("Accept-Charset", "UTF-8") // Accept-Charset 설정.
               //
                // urlConn!!.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8")

                // [2-2]. parameter 전달 및 데이터 읽어오기.
                val strParams = sbParams.toString() //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
                val os = urlConn!!.getOutputStream()
                os.write(strParams.toByteArray(charset("UTF-8"))) // 출력 스트림에 출력.
                os.flush() // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close() // 출력 스트림을 닫고 모든 시스템 자원을 해제.

                // [2-3]. 연결 요청 확인.
                // 실패 시 null을 리턴하고 메서드를 종료.
                if (urlConn!!.getResponseCode() !== HttpURLConnection.HTTP_OK)
                    return null

                // [2-4]. 읽어온 결과물 리턴.
                // 요청한 URL의 출력물을 BufferedReader로 받는다.
                val reader = BufferedReader(InputStreamReader(urlConn!!.getInputStream(), "UTF-8"))

                // 출력물의 라인과 그 합에 대한 변수.
                var line: String
                var page = ""

                // 라인을 받아와 합친다.
                for(line in reader.lines()){
                    page+=line
                }

                return page

            } catch (e: MalformedURLException) { // for URL.
                e.printStackTrace()
            } catch (e: IOException) { // for openConnection().
                e.printStackTrace()
            } finally {
                if (urlConn != null)
                    urlConn!!.disconnect()
            }

            return null

        }

    }*/
}
