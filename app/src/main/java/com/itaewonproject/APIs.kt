package com.itaewonproject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.receiver.Route
import java.io.IOException
import com.itaewonproject.model.receiver.Folder
import java.sql.Timestamp


object APIs{
    val bmp1 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStn_0SD5sQvwVChtojSWcxUR5Hkw5JMw4bNJsuoSiI2A1sahk49Q"
    val bmp2 = "https://www.fcbarcelona.com/photo-resources/fcbarcelona/photo/2019/04/27/2b3900dc-06d6-417c-906e-477241e279a8/mini_2019-04-27-BARCELONA-LEVANTE-69.JPG?width=1200&height=750"
    val bmp3 = "https://us.123rf.com/450wm/pandavector/pandavector1705/pandavector170502056/77588042-stock-illustration-multicolored-inable-balls-party-and-parties-single-icon-in-cartoon-style-rater-bitmap-symbol-stock-i.jpg?ver=6"
    val bmp4 = "https://us.123rf.com/450wm/pandavector/pandavector1705/pandavector170501946/77588232-%EB%85%B8%EB%9E%80%EC%83%89-%EC%86%9C-%ED%84%B8-%EB%B3%91%EC%95%84%EB%A6%AC-%EB%B6%80%ED%99%9C%EC%A0%88-%EB%8B%A8%EC%9D%BC-%EC%95%84%EC%9D%B4%EC%BD%98-%EB%A7%8C%ED%99%94-%EC%8A%A4%ED%83%80%EC%9D%BC-rater-%EB%B9%84%ED%8A%B8-%EB%A7%B5-%EA%B8%B0%ED%98%B8-%EC%A3%BC%EC%8B%9D-%EA%B7%B8%EB%A6%BC%EC%9E%85%EB%8B%88%EB%8B%A4-.jpg?ver=6"
    val bmp5 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQRSot03kQt2dLn0RdPM_18910imxh5xydnfj8quawFqKl4El5WQ"

    fun B_API1(userID:Int):ArrayList<Folder>{
        var arr= ArrayList<Folder>()
        var in1 = Route("InRoute1","바르셀로나",101, Timestamp(1479249799770))
        var in2 = Route("InRoute2","바르셀로나",102,Timestamp(System.currentTimeMillis()))
        var in3 = Route("InRoute3","바르셀로나",103,Timestamp(System.currentTimeMillis()))
        var in4 = Route("InRoute4","바르셀로나",101,Timestamp(System.currentTimeMillis()))
        var r1 = Route("바르셀로나 여행기1","바르셀로나",1,Timestamp(1479249799770))
        var r2 = Route("바르셀로나 여행기2","바르셀로나",2,Timestamp(System.currentTimeMillis()))
        var r3 = Route("바르셀로나 여행기3","바르셀로나",3,Timestamp(System.currentTimeMillis()))
        var r4 = Route("바르셀로나 여행기4","바르셀로나",4,Timestamp(System.currentTimeMillis()))
        var r5 = Route("바르셀로나 여행기5","바르셀로나",5,Timestamp(System.currentTimeMillis()))
        var r6 = Route("바르셀로나 여행기6","바르셀로나",6,Timestamp(System.currentTimeMillis()))
        arr.add(Folder("바르셀로나 폴더1","바르셀로나",1,arrayListOf(in1,in2,in3,in4)))
        arr.add(Folder("바르셀로나 폴더2","바르셀로나",2,arrayListOf(in1,in2,in3)))
        arr.add(Folder("바르셀로나 폴더3","바르셀로나",3,arrayListOf(in1,in3,in4)))
        arr.add(Folder("","바르셀로나",4,arrayListOf(r1)))
        arr.add(Folder("","바르셀로나",5,arrayListOf(r2)))
        arr.add(Folder("","바르셀로나",6,arrayListOf(r3)))
        arr.add(Folder("","바르셀로나",7,arrayListOf(r4)))
        arr.add(Folder("","바르셀로나",8,arrayListOf(r5)))
        arr.add(Folder("","바르셀로나",9,arrayListOf(r6)))
        arr.add(Folder("바르셀로나 폴더4","바르셀로나",10,arrayListOf(in1,in2,in3,in4)))
        arr.add(Folder("바르셀로나 폴더5","바르셀로나",11,arrayListOf(in1,in2,in3)))
        arr.add(Folder("바르셀로나 폴더6","바르셀로나",12,arrayListOf(in1,in3,in4)))
        Log.i("folder mock",Gson().toJson(arr))
        return arr
    }

    fun secToString(sec:Int):String {
        var ret = ""
        if(sec>=3600) ret+="${sec / 3600}시간"
        if (sec % 3600 != 0 || sec==0) ret += "${(sec % 3600) / 60}분"
        return ret
    }

    fun getCategoryImage(num:Int):Bitmap{
        var bmp = Bitmap.createBitmap(50,50,Bitmap.Config.ARGB_8888)
        when(num){
            0 -> bmp.eraseColor(Color.CYAN)
            1 -> bmp.eraseColor(Color.GREEN)
            2 -> bmp.eraseColor(Color.MAGENTA)
            3 -> bmp.eraseColor(Color.RED)
            4 -> bmp.eraseColor(Color.YELLOW)
        }
        return bmp
    }


}
