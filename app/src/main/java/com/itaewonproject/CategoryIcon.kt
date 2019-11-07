package com.itaewonproject

object CategoryIcon{
    val Icons = hashMapOf<LocationCategory,Int>()
    init{
        Icons.put(LocationCategory.UNKNOWN,R.drawable.ic_ico_dummy)
        Icons.put(LocationCategory.PUBLIC,R.drawable.ic_ico_tourist)
        Icons.put(LocationCategory.ENTERTAINMENT,R.drawable.ic_ico_shopping)//여가 아이콘 부재
        Icons.put(LocationCategory.UTILITY,R.drawable.ic_ico_store)
        Icons.put(LocationCategory.SERVICE,R.drawable.ic_ico_shopping)//여가 아이콘 부재
        Icons.put(LocationCategory.ACTIVITY,R.drawable.ic_ico_walk)
        Icons.put(LocationCategory.SHOPPING,R.drawable.ic_ico_shopping)
        Icons.put(LocationCategory.MEDICAL,R.drawable.ic_ico_hospital)
        Icons.put(LocationCategory.TRAFFIC,R.drawable.ic_ico_transfer)
        Icons.put(LocationCategory.ATTRACTION,R.drawable.ic_ico_tourist)
        Icons.put(LocationCategory.RELIGIOUS,R.drawable.ic_ico_tourist)
        Icons.put(LocationCategory.FOOD,R.drawable.ic_ico_restaurant)
        Icons.put(LocationCategory.LODGE,R.drawable.ic_ico_hotel)

    }
    inline fun get(category: LocationCategory):Int = Icons.get(category)!!
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