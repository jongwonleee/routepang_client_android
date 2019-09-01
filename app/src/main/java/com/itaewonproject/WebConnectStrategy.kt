package com.itaewonproject

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.sender.Link
import com.itaewonproject.model.receiver.Location

/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class WebConnectStrategy{
    abstract var method:String
    var domain:String
    abstract var inner:String
    abstract var param:String
    var statusCode=0

    init{
        domain = "http://ec2-52-79-237-33.ap-northeast-2.compute.amazonaws.com:9090/"
    }

    //abstract fun getResult(params:List<Any>):Any

    private fun getUrl(param:String):String{
        return domain+inner+param
    }



    inner class Task: AsyncTask<String, Integer, String>() {

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
