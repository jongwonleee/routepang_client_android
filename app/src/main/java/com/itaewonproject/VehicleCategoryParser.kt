package com.itaewonproject

import com.google.maps.model.VehicleType

object VehicleIcon{
    private val parser = hashMapOf<VehicleType,Int>()
    init{
        parser.put(VehicleType.BUS,R.drawable.ic_ico_vehicle_bus)
        parser.put(VehicleType.CABLE_CAR,R.drawable.ic_ico_vehicle_walk)
        parser.put(VehicleType.COMMUTER_TRAIN,R.drawable.ic_ico_vehicle_train)
        parser.put(VehicleType.FERRY,R.drawable.ic_ico_vehicle_walk)
        parser.put(VehicleType.FUNICULAR,R.drawable.ic_ico_vehicle_walk)
        parser.put(VehicleType.GONDOLA_LIFT,R.drawable.ic_ico_vehicle_walk)
        parser.put(VehicleType.HEAVY_RAIL,R.drawable.ic_ico_vehicle_subway)
        parser.put(VehicleType.HIGH_SPEED_TRAIN,R.drawable.ic_ico_vehicle_train)
        parser.put(VehicleType.INTERCITY_BUS,R.drawable.ic_ico_vehicle_bus)
        parser.put(VehicleType.METRO_RAIL,R.drawable.ic_ico_vehicle_subway)
        parser.put(VehicleType.MONORAIL,R.drawable.ic_ico_vehicle_subway)
        parser.put(VehicleType.OTHER,R.drawable.ic_ico_vehicle_walk)
        parser.put(VehicleType.RAIL,R.drawable.ic_ico_vehicle_subway)
        parser.put(VehicleType.SHARE_TAXI,R.drawable.ic_ico_vehicle_car)
        parser.put(VehicleType.SUBWAY,R.drawable.ic_ico_vehicle_subway)
        parser.put(VehicleType.TRAM,R.drawable.ic_ico_vehicle_train)
        parser.put(VehicleType.TROLLEYBUS,R.drawable.ic_ico_vehicle_bus)

    }

   fun get(category: VehicleType):Int = parser.get(category)!!
}