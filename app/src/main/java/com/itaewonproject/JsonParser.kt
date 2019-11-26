package com.itaewonproject

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
        if(!(result.responceCode==200 || result.responceCode==201))return arrayListOf()
        try {

            arr = Gson().fromJson(result.body, TypeToken.getParameterized(ArrayList::class.java, cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return if(arr==null) arrayListOf()
        else arr
    }
    fun <T> setJsonParsing(result: String, cls: Class<T>): Set<T> {
        var arr:Set<T>? = null
        try {

            arr = Gson().fromJson(result, TypeToken.getParameterized(Set::class.java, cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return if(arr==null) setOf()
        else arr
    }
}
