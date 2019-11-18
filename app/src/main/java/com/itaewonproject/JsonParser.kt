package com.itaewonproject

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.rests.WebResponce

class JsonParser {
    fun <T> objectJsonParsing(result: String, cls: Class<T>): T? {
        try {
            return Gson().fromJson(result, TypeToken.get(cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return null
    }
    fun <T> listJsonParsing(result: WebResponce, cls: Class<T>): ArrayList<T> {
        var arr:ArrayList<T>? = null
        Log.i("!!@@!!@@!!","0")

        if(!(result.responceCode==200 || result.responceCode==201))return arrayListOf()
        try {
            Log.i("!!@@!!@@!!","1")

            arr = Gson().fromJson(result.body, TypeToken.getParameterized(ArrayList::class.java, cls).type)
            Log.i("!!@@!!@@!!",arr!!.size.toString())
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        if(arr==null) return arrayListOf()
        else return arr
    }
}
