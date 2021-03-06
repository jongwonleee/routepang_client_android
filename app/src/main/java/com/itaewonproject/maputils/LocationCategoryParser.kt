package com.itaewonproject.maputils

object LocationCategoryParser{
    private val parser = hashMapOf<String, LocationCategory>()
    init{
        parser["accounting"] = LocationCategory.UNKNOWN
        parser["airport"] = LocationCategory.TRAFFIC
        parser["amusement_park"] = LocationCategory.ACTIVITY
        parser["aquarium"] = LocationCategory.ACTIVITY
        parser["art_gallery"] = LocationCategory.ACTIVITY
        parser["atm"] = LocationCategory.UTILITY
        parser["bakery"] = LocationCategory.FOOD
        parser["bank"] = LocationCategory.UTILITY
        parser["bar"] = LocationCategory.FOOD
        parser["beauty_salon"] = LocationCategory.SERVICE
        parser["bicycle_store"] = LocationCategory.SHOPPING
        parser["book_store"] = LocationCategory.SHOPPING
        parser["bowling_alley"] = LocationCategory.ENTERTAINMENT
        parser["bus_station"] = LocationCategory.TRAFFIC
        parser["cafe"] = LocationCategory.FOOD
        parser["campground"] = LocationCategory.ACTIVITY
        parser["car_dealer"] = LocationCategory.SHOPPING
        parser["car_rental"] = LocationCategory.UTILITY
        parser["car_repair"] = LocationCategory.UNKNOWN
        parser["car_wash"] = LocationCategory.UNKNOWN
        parser["casino"] = LocationCategory.ENTERTAINMENT
        parser["cemetery"] = LocationCategory.UNKNOWN
        parser["church"] = LocationCategory.RELIGIOUS
        parser["city_hall"] = LocationCategory.PUBLIC
        parser["clothing_store"] = LocationCategory.SHOPPING
        parser["convenience_store"] = LocationCategory.SHOPPING
        parser["courthouse"] = LocationCategory.PUBLIC
        parser["dentist"] = LocationCategory.MEDICAL
        parser["department_store"] = LocationCategory.SHOPPING
        parser["doctor"] = LocationCategory.MEDICAL
        parser["drugstore"] = LocationCategory.SHOPPING
        parser["electrician"] = LocationCategory.UNKNOWN
        parser["electronics_store"] = LocationCategory.SHOPPING
        parser["embassy"] = LocationCategory.UTILITY
        parser["fire_station"] = LocationCategory.UNKNOWN
        parser["florist"] = LocationCategory.SHOPPING
        parser["funeral_home"] = LocationCategory.UNKNOWN
        parser["furniture_store"] = LocationCategory.SHOPPING
        parser["gas_station"] = LocationCategory.UTILITY
        parser["grocery_or_supermarket"] = LocationCategory.SHOPPING
        parser["gym"] = LocationCategory.ACTIVITY
        parser["hair_care"] = LocationCategory.SERVICE
        parser["hardware_store"] = LocationCategory.SHOPPING
        parser["hindu_temple"] = LocationCategory.RELIGIOUS
        parser["home_goods_store"] = LocationCategory.SHOPPING
        parser["hospital"] = LocationCategory.MEDICAL
        parser["insurance_agency"] = LocationCategory.UNKNOWN
        parser["jewelry_store"] = LocationCategory.SHOPPING
        parser["laundry"] = LocationCategory.UTILITY
        parser["lawyer"] = LocationCategory.UNKNOWN
        parser["library"] = LocationCategory.ATTRACTION
        parser["light_rail_station"] = LocationCategory.TRAFFIC
        parser["liquor_store"] = LocationCategory.SHOPPING
        parser["local_government_office"] = LocationCategory.PUBLIC
        parser["locksmith"] = LocationCategory.UNKNOWN
        parser["lodging"] = LocationCategory.LODGE
        parser["meal_delivery"] = LocationCategory.FOOD
        parser["meal_takeaway"] = LocationCategory.FOOD
        parser["mosque"] = LocationCategory.RELIGIOUS
        parser["movie_rental"] = LocationCategory.ENTERTAINMENT
        parser["movie_theater"] = LocationCategory.ENTERTAINMENT
        parser["moving_company"] = LocationCategory.UNKNOWN
        parser["museum"] = LocationCategory.ACTIVITY
        parser["night_club"] = LocationCategory.ENTERTAINMENT
        parser["painter"] = LocationCategory.UNKNOWN
        parser["park"] = LocationCategory.PUBLIC
        parser["parking"] = LocationCategory.UTILITY
        parser["pet_store"] = LocationCategory.SHOPPING
        parser["pharmacy"] = LocationCategory.MEDICAL
        parser["physiotherapist"] = LocationCategory.MEDICAL
        parser["plumber"] = LocationCategory.UNKNOWN
        parser["police"] = LocationCategory.PUBLIC
        parser["post_office"] = LocationCategory.PUBLIC
        parser["primary_school"] = LocationCategory.UNKNOWN
        parser["real_estate_agency"] = LocationCategory.UNKNOWN
        parser["restaurant"] = LocationCategory.FOOD
        parser["roofing_contractor"] = LocationCategory.UNKNOWN
        parser["rv_park"] = LocationCategory.ACTIVITY
        parser["school"] = LocationCategory.UNKNOWN
        parser["secondary_school"] = LocationCategory.UNKNOWN
        parser["shoe_store"] = LocationCategory.SHOPPING
        parser["shopping_mall"] = LocationCategory.SHOPPING
        parser["spa"] = LocationCategory.SERVICE
        parser["stadium"] = LocationCategory.ACTIVITY
        parser["storage"] = LocationCategory.UNKNOWN
        parser["store"] = LocationCategory.SHOPPING
        parser["subway_station"] = LocationCategory.TRAFFIC
        parser["supermarket"] = LocationCategory.SHOPPING
        parser["synagogue"] = LocationCategory.RELIGIOUS
        parser["taxi_stand"] = LocationCategory.TRAFFIC
        parser["tourist_attraction"] = LocationCategory.ATTRACTION
        parser["train_station"] = LocationCategory.TRAFFIC
        parser["transit_station"] = LocationCategory.TRAFFIC
        parser["travel_agency"] = LocationCategory.ACTIVITY
        parser["university"] = LocationCategory.PUBLIC
        parser["veterinary_care"] = LocationCategory.MEDICAL
        parser["zoo"] = LocationCategory.ACTIVITY

        parser["administrative_area_level_LocationCategory.ATTRACTION"] = LocationCategory.UNKNOWN
        parser["administrative_area_level_LocationCategory.FOOD"] = LocationCategory.UNKNOWN
        parser["administrative_area_level_3"] = LocationCategory.UNKNOWN
        parser["administrative_area_level_4"] = LocationCategory.UNKNOWN
        parser["administrative_area_level_5"] = LocationCategory.UNKNOWN
        parser["archipelago"] = LocationCategory.UNKNOWN
        parser["colloquial_area"] = LocationCategory.UNKNOWN
        parser["continent"] = LocationCategory.UNKNOWN
        parser["country"] = LocationCategory.UNKNOWN
        parser["establishment"] = LocationCategory.FOOD
        parser["finance"] = LocationCategory.ATTRACTION
        parser["floor"] = LocationCategory.UNKNOWN
        parser["food"] = LocationCategory.FOOD
        parser["general_contractor"] = LocationCategory.UNKNOWN
        parser["geocode"] = LocationCategory.UNKNOWN
        parser["health"] = LocationCategory.MEDICAL
        parser["intersection"] = LocationCategory.UNKNOWN
        parser["locality"] = LocationCategory.UNKNOWN
        parser["natural_feature"] = LocationCategory.ATTRACTION
        parser["neighborhood"] = LocationCategory.UNKNOWN
        parser["place_of_worship"] = LocationCategory.RELIGIOUS
        parser["point_of_interest"] = LocationCategory.ATTRACTION
        parser["political"] = LocationCategory.UNKNOWN
        parser["post_box"] = LocationCategory.UNKNOWN
        parser["postal_code"] = LocationCategory.PUBLIC
        parser["postal_code_prefix"] = LocationCategory.UNKNOWN
        parser["postal_code_suffix"] = LocationCategory.UNKNOWN
        parser["postal_town"] = LocationCategory.PUBLIC
        parser["premise"] = LocationCategory.UNKNOWN
        parser["room"] = LocationCategory.UNKNOWN
        parser["route"] = LocationCategory.UNKNOWN
        parser["street_address"] = LocationCategory.UNKNOWN
        parser["street_number"] = LocationCategory.UNKNOWN
        parser["sublocality"] = LocationCategory.UNKNOWN
        parser["sublocality_level_LocationCategory.ATTRACTION"] = LocationCategory.UNKNOWN
        parser["sublocality_level_LocationCategory.FOOD"] = LocationCategory.UNKNOWN
        parser["sublocality_level_3"] = LocationCategory.UNKNOWN
        parser["sublocality_level_4"] = LocationCategory.UNKNOWN
        parser["sublocality_level_5"] = LocationCategory.UNKNOWN
        parser["subpremise"] = LocationCategory.UNKNOWN
    }

   fun get(category: String): LocationCategory {
      var ret = LocationCategory.UNKNOWN
      if(parser.containsKey(category)) ret = parser[category]!!
      return ret
   }

}