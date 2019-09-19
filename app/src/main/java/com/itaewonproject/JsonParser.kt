package com.itaewonproject

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

class JsonParser {
    fun <T> objectJsonParsing(apiResult: String, cls: Class<T>): T? {
        try {
            return Gson().fromJson(apiResult, TypeToken.get(cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return null
    }
    fun <T> listJsonParsing(apiResult: String, cls: Class<T>): ArrayList<T> {
        var arr = arrayListOf<T>()
        try {
            arr = Gson().fromJson(apiResult, TypeToken.getParameterized(ArrayList::class.java, cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return arr
    }
}
