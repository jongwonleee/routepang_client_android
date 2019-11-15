package com.itaewonproject.maputils

import android.graphics.Color
import com.itaewonproject.R

object CategoryIcon{
    private val icons = hashMapOf<LocationCategory,Int>()
    private val indexs = HashMap<LocationCategory,Int>()
    private val colors = arrayOf("#4DABFF","#4DABFF","#866DF2","#FB7286","#F8C51B")
    init{
        icons.put(
            LocationCategory.UNKNOWN,
            R.drawable.ic_ico_dummy
        )
        icons.put(
            LocationCategory.PUBLIC,
            R.drawable.ic_ico_tourist
        )
        icons.put(
            LocationCategory.ENTERTAINMENT,
            R.drawable.ic_ico_shopping
        )//여가 아이콘 부재
        icons.put(
            LocationCategory.UTILITY,
            R.drawable.ic_ico_store
        )
        icons.put(
            LocationCategory.SERVICE,
            R.drawable.ic_ico_shopping
        )//여가 아이콘 부재
        icons.put(
            LocationCategory.ACTIVITY,
            R.drawable.ic_ico_walk
        )
        icons.put(
            LocationCategory.SHOPPING,
            R.drawable.ic_ico_shopping
        )
        icons.put(
            LocationCategory.MEDICAL,
            R.drawable.ic_ico_hospital
        )
        icons.put(
            LocationCategory.TRAFFIC,
            R.drawable.ic_ico_transfer
        )
        icons.put(
            LocationCategory.ATTRACTION,
            R.drawable.ic_ico_tourist
        )
        icons.put(
            LocationCategory.RELIGIOUS,
            R.drawable.ic_ico_tourist
        )
        icons.put(
            LocationCategory.FOOD,
            R.drawable.ic_ico_restaurant
        )
        icons.put(
            LocationCategory.LODGE,
            R.drawable.ic_ico_hotel
        )

        // 파 초 보 빨
        indexs.put(LocationCategory.UNKNOWN,4)
        indexs.put(LocationCategory.PUBLIC,0)
        indexs.put(LocationCategory.ENTERTAINMENT,3)//여가 아이콘 부재
        indexs.put(LocationCategory.UTILITY,2)
        indexs.put(LocationCategory.SERVICE,3)//여가 아이콘 부재
        indexs.put(LocationCategory.ACTIVITY,0)
        indexs.put(LocationCategory.SHOPPING,3)
        indexs.put(LocationCategory.MEDICAL,2)
        indexs.put(LocationCategory.TRAFFIC,2)
        indexs.put(LocationCategory.ATTRACTION,0)
        indexs.put(LocationCategory.RELIGIOUS,0)
        indexs.put(LocationCategory.FOOD,3)
        indexs.put(LocationCategory.LODGE,1)

    }
    fun get(category: LocationCategory):Int = icons.get(category)!!

    fun getIndex(category: LocationCategory):Int = indexs.get(category)!!

    fun getColor(category: LocationCategory):Int = Color.parseColor(colors[indexs.get(category)!!])
}

/***
음식 (FOOD,)
쇼핑 (SHOPPING,)
여가 - 오락, 서비스 (ENTERTAINMENT, SETVICE)

여행 - 명소, 종교, 공공시설( ATTRACTION, RELIGIOUS, PUBLIC)
활동 (ACTIVITY)

호텔 (LODGE)

여행중요 의료, 여행중요시설 (MEDICAL, UTILITY)
교통 (TRAFFIC)

더미아이콘 (UNKNOWN)

아이콘 만들기

3 3 수정

 **/