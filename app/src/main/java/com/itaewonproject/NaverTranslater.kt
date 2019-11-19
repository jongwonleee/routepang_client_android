package com.itaewonproject

import android.util.Log
import com.itaewonproject.rests.IS_OFFLINE
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class NaverTranslater(private val clientId:String, private val clientSecret:String) {


    fun get(text:String): String{

        if (IS_OFFLINE) return "notTranslated"
        else {
            try {
                val text = URLEncoder.encode(text, "UTF-8")
                val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
                val url = URL(apiURL)
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("X-Naver-Client-Id",clientId )
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
                // post request
                val postParams = "source=ko&target=en&text=$text"
                con.doOutput = true
                val wr = DataOutputStream(con.outputStream)
                wr.writeBytes(postParams)
                wr.flush()
                wr.close()
                val responseCode = con.responseCode
                val br: BufferedReader
                if (responseCode == 200) { // 정상 호출
                    Log.i("naver!!","no error")
                    br = BufferedReader(InputStreamReader(con.inputStream))

                } else {  // 에러 발생
                    br = BufferedReader(InputStreamReader(con.errorStream))
                    return "notTranslated"
                }
                val ret = br.use(BufferedReader::readText)
                br.close()
                return ret
            } catch (e: Exception) {
                println(e)
                return "notTranslated"
            }
        }
    }

}
