
package com.itaewonproject.rests

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.*


var authorization= ""
class HttpClient(val contents:ArrayList<Pair<String,String>>) {

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
            if (httpStatusCode == HttpURLConnection.HTTP_OK)
                body = readStream(conn)
            else {
                Log.i("httpError", "$httpStatusCode, ${HttpURLConnection.HTTP_OK}, ${conn.responseCode}")
            }
            conn.disconnect()
        }

        private fun setHeader(connection: HttpURLConnection?) {
            setContentType(connection!!)
            setRequestMethod(connection)

            connection.connectTimeout = 3000
            connection.doInput = true
        }

        private fun setContentType(connection: HttpURLConnection) {
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", authorization)
            for(c in contents){
                connection.setRequestProperty(c.first,c.second)
            }
            contents.clear()
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
            Log.i("setting body", parameter)
            if (parameter.isNotEmpty()) {
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
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: ConnectException) {
                e.printStackTrace()
            }

            return -10
        }

        private fun readStream(connection: HttpURLConnection): String {
            var result = ""
            var reader: BufferedReader? = null
            try {
                Log.i("inputS", connection.inputStream.toString())
                reader = BufferedReader(InputStreamReader(connection.inputStream))
                for (line in reader.readLines()) {
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

        class Builder(method: String, val url: String) {

            private var parameters: String
            val method: String
            val contents = arrayListOf<Pair<String,String>>()

            /* private val keys: Iterator<String>
                 get() = this.parameters.keys.iterator()
    */
            init {
                this.method = method
                parameters = ""
                // this.parameters = HashMap()
            }

            fun setParameters(param: String?) {
                if (param != null) {
                    this.parameters = param
                }
            }

            fun getParameters(): String {
                return parameters
            }

            fun addContentType(content:Pair<String,String>) {
                contents.add(content)

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
                val client = HttpClient(contents)
                client.setBuilder(this)
                return client
            }
        }
}
