package com.itaewonproject.maputils

object LocationCategoryParser{
    private val parser = hashMapOf<String, LocationCategory>()
    init{
       parser.put("accounting",
           LocationCategory.UNKNOWN
       )
       parser.put("airport",
           LocationCategory.TRAFFIC
       )
       parser.put("amusement_park",
           LocationCategory.ACTIVITY
       )
       parser.put("aquarium",
           LocationCategory.ACTIVITY
       )
       parser.put("art_gallery",
           LocationCategory.ACTIVITY
       )
       parser.put("atm",
           LocationCategory.UTILITY
       )
       parser.put("bakery",
           LocationCategory.FOOD
       )
       parser.put("bank",
           LocationCategory.UTILITY
       )
       parser.put("bar",
           LocationCategory.FOOD
       )
       parser.put("beauty_salon",
           LocationCategory.SERVICE
       )
       parser.put("bicycle_store",
           LocationCategory.SHOPPING
       )
       parser.put("book_store",
           LocationCategory.SHOPPING
       )
       parser.put("bowling_alley",
           LocationCategory.ENTERTAINMENT
       )
       parser.put("bus_station",
           LocationCategory.TRAFFIC
       )
       parser.put("cafe",
           LocationCategory.FOOD
       )
       parser.put("campground",
           LocationCategory.ACTIVITY
       )
       parser.put("car_dealer",
           LocationCategory.SHOPPING
       )
       parser.put("car_rental",
           LocationCategory.UTILITY
       )
       parser.put("car_repair",
           LocationCategory.UNKNOWN
       )
       parser.put("car_wash",
           LocationCategory.UNKNOWN
       )
       parser.put("casino",
           LocationCategory.ENTERTAINMENT
       )
       parser.put("cemetery",
           LocationCategory.UNKNOWN
       )
       parser.put("church",
           LocationCategory.RELIGIOUS
       )
       parser.put("city_hall",
           LocationCategory.PUBLIC
       )
       parser.put("clothing_store",
           LocationCategory.SHOPPING
       )
       parser.put("convenience_store",
           LocationCategory.SHOPPING
       )
       parser.put("courthouse",
           LocationCategory.PUBLIC
       )
       parser.put("dentist",
           LocationCategory.MEDICAL
       )
       parser.put("department_store",
           LocationCategory.SHOPPING
       )
       parser.put("doctor",
           LocationCategory.MEDICAL
       )
       parser.put("drugstore",
           LocationCategory.SHOPPING
       )
       parser.put("electrician",
           LocationCategory.UNKNOWN
       )
       parser.put("electronics_store",
           LocationCategory.SHOPPING
       )
       parser.put("embassy",
           LocationCategory.UTILITY
       )
       parser.put("fire_station",
           LocationCategory.UNKNOWN
       )
       parser.put("florist",
           LocationCategory.SHOPPING
       )
       parser.put("funeral_home",
           LocationCategory.UNKNOWN
       )
       parser.put("furniture_store",
           LocationCategory.SHOPPING
       )
       parser.put("gas_station",
           LocationCategory.UTILITY
       )
       parser.put("grocery_or_supermarket",
           LocationCategory.SHOPPING
       )
       parser.put("gym",
           LocationCategory.ACTIVITY
       )
       parser.put("hair_care",
           LocationCategory.SERVICE
       )
       parser.put("hardware_store",
           LocationCategory.SHOPPING
       )
       parser.put("hindu_temple",
           LocationCategory.RELIGIOUS
       )
       parser.put("home_goods_store",
           LocationCategory.SHOPPING
       )
       parser.put("hospital",
           LocationCategory.MEDICAL
       )
       parser.put("insurance_agency",
           LocationCategory.UNKNOWN
       )
       parser.put("jewelry_store",
           LocationCategory.SHOPPING
       )
       parser.put("laundry",
           LocationCategory.UTILITY
       )
       parser.put("lawyer",
           LocationCategory.UNKNOWN
       )
       parser.put("library",
           LocationCategory.ATTRACTION
       )
       parser.put("light_rail_station",
           LocationCategory.TRAFFIC
       )
       parser.put("liquor_store",
           LocationCategory.SHOPPING
       )
       parser.put("local_government_office",
           LocationCategory.PUBLIC
       )
       parser.put("locksmith",
           LocationCategory.UNKNOWN
       )
       parser.put("lodging",
           LocationCategory.LODGE
       )
       parser.put("meal_delivery",
           LocationCategory.FOOD
       )
       parser.put("meal_takeaway",
           LocationCategory.FOOD
       )
       parser.put("mosque",
           LocationCategory.RELIGIOUS
       )
       parser.put("movie_rental",
           LocationCategory.ENTERTAINMENT
       )
       parser.put("movie_theater",
           LocationCategory.ENTERTAINMENT
       )
       parser.put("moving_company",
           LocationCategory.UNKNOWN
       )
       parser.put("museum",
           LocationCategory.ACTIVITY
       )
       parser.put("night_club",
           LocationCategory.ENTERTAINMENT
       )
       parser.put("painter",
           LocationCategory.UNKNOWN
       )
       parser.put("park",
           LocationCategory.PUBLIC
       )
       parser.put("parking",
           LocationCategory.UTILITY
       )
       parser.put("pet_store",
           LocationCategory.SHOPPING
       )
       parser.put("pharmacy",
           LocationCategory.MEDICAL
       )
       parser.put("physiotherapist",
           LocationCategory.MEDICAL
       )
       parser.put("plumber",
           LocationCategory.UNKNOWN
       )
       parser.put("police",
           LocationCategory.PUBLIC
       )
       parser.put("post_office",
           LocationCategory.PUBLIC
       )
       parser.put("primary_school",
           LocationCategory.UNKNOWN
       )
       parser.put("real_estate_agency",
           LocationCategory.UNKNOWN
       )
       parser.put("restaurant",
           LocationCategory.FOOD
       )
       parser.put("roofing_contractor",
           LocationCategory.UNKNOWN
       )
       parser.put("rv_park",
           LocationCategory.ACTIVITY
       )
       parser.put("school",
           LocationCategory.UNKNOWN
       )
       parser.put("secondary_school",
           LocationCategory.UNKNOWN
       )
       parser.put("shoe_store",
           LocationCategory.SHOPPING
       )
       parser.put("shopping_mall",
           LocationCategory.SHOPPING
       )
       parser.put("spa",
           LocationCategory.SERVICE
       )
       parser.put("stadium",
           LocationCategory.ACTIVITY
       )
       parser.put("storage",
           LocationCategory.UNKNOWN
       )
       parser.put("store",
           LocationCategory.SHOPPING
       )
       parser.put("subway_station",
           LocationCategory.TRAFFIC
       )
       parser.put("supermarket",
           LocationCategory.SHOPPING
       )
       parser.put("synagogue",
           LocationCategory.RELIGIOUS
       )
       parser.put("taxi_stand",
           LocationCategory.TRAFFIC
       )
       parser.put("tourist_attraction", LocationCategory.ATTRACTION)
       parser.put("train_station", LocationCategory.TRAFFIC)
       parser.put("transit_station", LocationCategory.TRAFFIC)
       parser.put("travel_agency", LocationCategory.ACTIVITY)
       parser.put("university", LocationCategory.PUBLIC)
       parser.put("veterinary_care", LocationCategory.MEDICAL)
       parser.put("zoo", LocationCategory.ACTIVITY)

       parser.put("administrative_area_level_LocationCategory.ATTRACTION" , LocationCategory.UNKNOWN)
       parser.put("administrative_area_level_LocationCategory.FOOD" , LocationCategory.UNKNOWN)
       parser.put("administrative_area_level_3" , LocationCategory.UNKNOWN)
       parser.put("administrative_area_level_4" , LocationCategory.UNKNOWN)
       parser.put("administrative_area_level_5" , LocationCategory.UNKNOWN)
       parser.put("archipelago" , LocationCategory.UNKNOWN)
       parser.put("colloquial_area" , LocationCategory.UNKNOWN)
       parser.put("continent" , LocationCategory.UNKNOWN)
       parser.put("country" , LocationCategory.UNKNOWN)
       parser.put("establishment" , LocationCategory.FOOD)
       parser.put("finance" , LocationCategory.ATTRACTION)
       parser.put("floor" , LocationCategory.UNKNOWN)
       parser.put("food" , LocationCategory.FOOD)
       parser.put("general_contractor" , LocationCategory.UNKNOWN)
       parser.put("geocode" , LocationCategory.UNKNOWN)
       parser.put("health" , LocationCategory.MEDICAL)
       parser.put("intersection" , LocationCategory.UNKNOWN)
       parser.put("locality" , LocationCategory.UNKNOWN)
       parser.put("natural_feature" , LocationCategory.ATTRACTION)
       parser.put("neighborhood" , LocationCategory.UNKNOWN)
       parser.put("place_of_worship" , LocationCategory.RELIGIOUS)
       parser.put("point_of_interest" , LocationCategory.ATTRACTION)
       parser.put("political" , LocationCategory.UNKNOWN)
       parser.put("post_box" , LocationCategory.UNKNOWN)
       parser.put("postal_code" , LocationCategory.PUBLIC)
       parser.put("postal_code_prefix" , LocationCategory.UNKNOWN)
       parser.put("postal_code_suffix" , LocationCategory.UNKNOWN)
       parser.put("postal_town" , LocationCategory.PUBLIC)
       parser.put("premise" , LocationCategory.UNKNOWN)
       parser.put("room" , LocationCategory.UNKNOWN)
       parser.put("route" , LocationCategory.UNKNOWN)
       parser.put("street_address" , LocationCategory.UNKNOWN)
       parser.put("street_number" , LocationCategory.UNKNOWN)
       parser.put("sublocality" , LocationCategory.UNKNOWN)
       parser.put("sublocality_level_LocationCategory.ATTRACTION" , LocationCategory.UNKNOWN)
       parser.put("sublocality_level_LocationCategory.FOOD" , LocationCategory.UNKNOWN)
       parser.put("sublocality_level_3" , LocationCategory.UNKNOWN)
       parser.put("sublocality_level_4" , LocationCategory.UNKNOWN)
       parser.put("sublocality_level_5" , LocationCategory.UNKNOWN)
       parser.put("subpremise" , LocationCategory.UNKNOWN)
    }

   fun get(category: String): LocationCategory {
      var ret = LocationCategory.UNKNOWN
      if(parser.containsKey(category)) ret = parser.get(category)!!
      return ret
   }

}