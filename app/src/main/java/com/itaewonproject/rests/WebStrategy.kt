package com.itaewonproject.rests

import android.os.AsyncTask
import android.util.Log

const val IS_OFFLINE: Boolean = true
const val CLASS_DOMAIN:String = "http://www.routepang.com:9090/"
/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class WebStrategy {
    var domain: String
    abstract val inner: String
    abstract var param: String
    abstract val mockData: String
    var statusCode: Int? = null
    abstract val method:String
    companion object {
        val classDomain: String = CLASS_DOMAIN
        val isOffline: Boolean = IS_OFFLINE

    }

    init {
        domain = classDomain
    }

    private fun createUrl(param: String): String {

        return domain + inner + param
    }

    inner class Task : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg p0: String?): String {
            if (isOffline){
                statusCode=200
                return mockData
            }
            else {
                val http =
                    HttpClient.Builder(method, createUrl(param)) // 포트번호,서블릿주소
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
