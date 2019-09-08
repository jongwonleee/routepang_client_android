package com.itaewonproject

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Link
import java.lang.reflect.Type
import kotlin.reflect.KClass

class JsonParser{
    fun <T> objectJsonParsing(apiResult:String,cls:Class<T>):T? {
        try {
            return Gson().fromJson(apiResult, TypeToken.get(cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return null
    }
    fun <T> listJsonParsing(apiResult:String,cls:Class<T>):ArrayList<T> {
        var arr = arrayListOf<T>()
        try {
            arr = Gson().fromJson(apiResult, TypeToken.getParameterized(ArrayList::class.java,cls).type)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return arr
    }
}