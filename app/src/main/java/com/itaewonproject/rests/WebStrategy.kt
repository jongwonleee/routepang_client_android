package com.itaewonproject.rests

import android.os.AsyncTask
import android.util.Log

const val IS_OFFLINE: Boolean = false
const val CLASS_DOMAIN:String = "http://www.routepang.com:9090/"
/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */

abstract class WebStrategy {
    protected var domain: String
    protected abstract val inner: String
    protected abstract var param: String
    protected abstract val mockData: String
    protected var statusCode: Int? = null
    protected var contents= arrayListOf<Pair<String,String>>()
    abstract val method:String
    companion object {
        const val classDomain: String = CLASS_DOMAIN
        const val isOffline: Boolean = IS_OFFLINE

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
                Log.i("rest url", method+":: "+http.url)
                // Parameter 를 전송한다.
                if (p0.isNotEmpty())
                    http.setParameters(p0[0])


                for(c in contents){
                    http.addContentType(c)
                }


                // Http 요청 전송
                val post = http.create()

                post.request()

                Log.i("!!postBody", post.body)

                // 응답 상태코드 가져오기
                statusCode = post.httpStatusCode
                Log.i("rest status",statusCode.toString())
                // 응답 본문 가져오기
                return post.body
            }
        }
    }
}
