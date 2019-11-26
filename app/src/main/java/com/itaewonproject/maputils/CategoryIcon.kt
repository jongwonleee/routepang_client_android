package com.itaewonproject.maputils

import android.graphics.Color
import com.itaewonproject.R

object CategoryIcon{
    private val icons = hashMapOf<LocationCategory,Int>()
    private val indexs = HashMap<LocationCategory,Int>()
    private val colors = arrayOf("#4DABFF","#23A80C","#866DF2","#FB7286","#F8C51B")
    init{
        icons[LocationCategory.UNKNOWN] = R.drawable.ic_ico_dummy
        icons[LocationCategory.PUBLIC] = R.drawable.ic_ico_tourist
        icons[LocationCategory.ENTERTAINMENT] = R.drawable.ic_ico_shopping//여가 아이콘 부재
        icons[LocationCategory.UTILITY] = R.drawable.ic_ico_store
        icons[LocationCategory.SERVICE] = R.drawable.ic_ico_shopping//여가 아이콘 부재
        icons[LocationCategory.ACTIVITY] = R.drawable.ic_ico_walk
        icons[LocationCategory.SHOPPING] = R.drawable.ic_ico_shopping
        icons[LocationCategory.MEDICAL] = R.drawable.ic_ico_hospital
        icons[LocationCategory.TRAFFIC] = R.drawable.ic_ico_transfer
        icons[LocationCategory.ATTRACTION] = R.drawable.ic_ico_tourist
        icons[LocationCategory.RELIGIOUS] = R.drawable.ic_ico_tourist
        icons[LocationCategory.FOOD] = R.drawable.ic_ico_restaurant
        icons[LocationCategory.LODGE] = R.drawable.ic_ico_hotel

        // 파 초 보 빨
        indexs[LocationCategory.UNKNOWN] = 4
        indexs[LocationCategory.PUBLIC] = 0
        indexs[LocationCategory.ENTERTAINMENT] = 3//여가 아이콘 부재
        indexs[LocationCategory.UTILITY] = 2
        indexs[LocationCategory.SERVICE] = 3//여가 아이콘 부재
        indexs[LocationCategory.ACTIVITY] = 0
        indexs[LocationCategory.SHOPPING] = 3
        indexs[LocationCategory.MEDICAL] = 2
        indexs[LocationCategory.TRAFFIC] = 2
        indexs[LocationCategory.ATTRACTION] = 0
        indexs[LocationCategory.RELIGIOUS] = 0
        indexs[LocationCategory.FOOD] = 3
        indexs[LocationCategory.LODGE] = 1

    }
    fun get(category: LocationCategory):Int = icons[category]!!

    fun getIndex(category: LocationCategory):Int = indexs[category]!!

    fun getColor(category: LocationCategory):Int = Color.parseColor(colors[indexs[category]!!])
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