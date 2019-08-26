package com.itaewonproject

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.ServerResult.Article
import com.itaewonproject.ServerModel.Link
import com.itaewonproject.ServerResult.Location
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.Exception
import java.net.*

abstract class API<T:Any>{
    abstract var method:String
    var domain:String
    abstract var inner:String
    abstract var param:String
    var statusCode=0
    init{
        domain = "http://ec2-13-125-246-249.ap-northeast-2.compute.amazonaws.com:9090/"
    }

    fun getUrl(param:String):String{
        return domain+inner+param
    }

    /*fun jsonParsing(apiResult:String):ArrayList<T>{
        var arr = ArrayList<T>()
        var gson = Gson()
        try{
            arr.addAll(gson.fromJson(apiResult, object : TypeToken<ArrayList<T>>(){}.type))
        }catch(e: JsonParseException)
        {
            e.printStackTrace()
        }
        return arr
    }*/
    fun locationJsonParsing(apiResult:String):ArrayList<Location>{
        var arr = ArrayList<Location>()
        var gson = Gson()
        try{
            arr.addAll(gson.fromJson(apiResult, object : TypeToken<ArrayList<Location>>(){}.type))
        }catch(e: JsonParseException)
        {
            e.printStackTrace()
        }
        return arr
    }
    fun articleJsonParsing(apiResult:String):ArrayList<Article>{
        var arr = ArrayList<Article>()
        var gson = Gson()
        try{
            arr.addAll(gson.fromJson(apiResult, object : TypeToken<ArrayList<Article>>(){}.type))
        }catch(e: JsonParseException)
        {
            e.printStackTrace()
        }
        return arr
    }
    fun linkJsonParsing(apiResult:String): Link {
        var arr = Link()
        var gson = Gson()
        try{
            Log.i("!!!'",apiResult)
            arr = gson.fromJson(apiResult, object : TypeToken<Link>(){}.type)
        }catch(e: JsonParseException)
        {
            e.printStackTrace()
        }
        return arr
    }

    inner class Task: AsyncTask<String, Integer, String>() {
        val ip = "localhost"
        override fun doInBackground(vararg p0: String?): String {
            val http = HttpClient.Builder(method, getUrl(param)) //포트번호,서블릿주소
            Log.i("!!url",http.url)
            // Parameter 를 전송한다.
            if(p0.size>0)
                http.setParameters(p0[0])


            //Http 요청 전송
            val post = http.create()

            post.request()

            Log.i("!!postBody",post.body)


            // 응답 상태코드 가져오기
            statusCode = post.httpStatusCode

            // 응답 본문 가져오기
            return post.body
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }

}
    class HttpClient {

        var httpStatusCode: Int = 0
        var body: String = ""

        private var builder: Builder? = null

        private val connection: HttpURLConnection?
            get() {
                try {
                    val url = URL(builder!!.url)
                    return url.openConnection() as HttpURLConnection
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return null
            }

        private fun setBuilder(builder: Builder) {
            this.builder = builder

        }

        fun request() {
            val conn = connection
            setHeader(conn)
            setBody(conn)
            httpStatusCode = getStatusCode(conn!!)
            if(httpStatusCode== HttpURLConnection.HTTP_OK)
                body = readStream(conn)
            else
            {
                Log.i("httpError","$httpStatusCode, ${HttpURLConnection.HTTP_OK}, ${conn.responseCode}")
            }
            conn.disconnect()
        }

        private fun setHeader(connection: HttpURLConnection?) {
            setContentType(connection!!)
            setRequestMethod(connection)

            connection.connectTimeout = 5000
            connection.doInput = true
        }

        private fun setContentType(connection: HttpURLConnection) {
            connection.setRequestProperty("Content-Type","application/json")
        }

        private fun setRequestMethod(connection: HttpURLConnection) {
            try {
                connection.requestMethod = builder!!.method
            } catch (e: ProtocolException) {
                e.printStackTrace()
            }

        }

        private fun setBody(connection: HttpURLConnection?) {

            val parameter = builder!!.getParameters()
            Log.i("setting body",parameter)
            if (parameter != null && parameter.length > 0) {
                var outputStream: OutputStream? = null
                try {
                    outputStream = connection!!.outputStream
                    outputStream!!.write(parameter.toByteArray(charset("UTF-8")))
                    outputStream.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        outputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

        }

        private fun getStatusCode(connection: HttpURLConnection): Int {
            try {
                return connection.responseCode
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e:Exception){
                e.printStackTrace()
            } catch (e:ConnectException){
                e.printStackTrace()
            }

            return -10
        }

        private fun readStream(connection: HttpURLConnection): String {
            var result = ""
            var reader: BufferedReader? = null
            try {
                Log.i("inputS",connection.inputStream.toString())
                reader = BufferedReader(InputStreamReader(connection.inputStream))
                for (line in reader.readLines()){
                    result += line
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    reader?.close()
                } catch (e: IOException) {
                }

            }

            return result
        }

        class Builder(method:String, val url:String) {

            private var parameters: String
            val method: String

            /* private val keys: Iterator<String>
                 get() = this.parameters.keys.iterator()
    */
            init {
                this.method = method
                parameters=""
                //this.parameters = HashMap()
            }


            fun setParameters(param: String?) {
                if (param != null) {
                    this.parameters = param
                }
            }

            fun appendParameter(param: String){
                this.parameters.plus(param)
            }


            fun getParameters(): String {
                return parameters
            }



            /*private fun generateParameters(): String {
                val parameters = StringBuffer()

                val keys = keys

                *//*var key = ""
                while (keys.hasNext()) {
                    key = keys.next()
                    parameters.append(String.format("%s=%s", key, this.parameters[key]))
                    parameters.append("&")
                }

                var params = parameters.toString()
                if (params.length > 0) {
                    params = params.substring(0, params.length - 1)
                }*//*
                var point = GeometryFactory().createPoint(Coordinate(123.33333,23.44444444))
                parameters.append("")
                var params = parameters.toString()
                return params
            }
*/
            fun create(): HttpClient {
                val client = HttpClient()
                client.setBuilder(this)
                return client
            }
        }
}