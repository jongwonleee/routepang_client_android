package com.itaewonproject

import android.os.AsyncTask
import android.util.Log

const val IS_OFFLINE: Boolean = false

/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class WebConnectStrategy {
    abstract var method: String
    var domain: String
    abstract var inner: String
    abstract var param: String
    abstract var mockData: String
    var isOffline: Boolean = IS_OFFLINE
    var statusCode: Int? = null
    companion object {
        val classDomain: String = "http://www.routepang.com:9090/"
    }

    abstract fun get(vararg params: Any): String

    init {
        domain = WebConnectStrategy.classDomain
    }

    private fun createUrl(param: String): String {

        return domain + inner + param
    }

    inner class Task : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg p0: String?): String {
            if (isOffline) return mockData
            else {
                val http = HttpClient.Builder(method, createUrl(param)) // 포트번호,서블릿주소
                Log.i("!!url", http.url)
                // Parameter 를 전송한다.
                if (p0.size> 0)
                    http.setParameters(p0[0])

                // Http 요청 전송
                val post = http.create()

                post.request()

                Log.i("!!postBody", post.body)

                // 응답 상태코드 가져오기
                statusCode = post.httpStatusCode

                // 응답 본문 가져오기
                return post.body
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }
}
