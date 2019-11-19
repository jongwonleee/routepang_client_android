package com.itaewonproject.maputils

import com.google.maps.model.VehicleType
import com.itaewonproject.R

object VehicleIcon{
    private val parser = hashMapOf<VehicleType,Int>()
    init{
        parser[VehicleType.BUS] = R.drawable.ic_ico_vehicle_bus
        parser[VehicleType.CABLE_CAR] = R.drawable.ic_ico_vehicle_walk
        parser[VehicleType.COMMUTER_TRAIN] = R.drawable.ic_ico_vehicle_train
        parser[VehicleType.FERRY] = R.drawable.ic_ico_vehicle_walk
        parser[VehicleType.FUNICULAR] = R.drawable.ic_ico_vehicle_walk
        parser[VehicleType.GONDOLA_LIFT] = R.drawable.ic_ico_vehicle_walk
        parser[VehicleType.HEAVY_RAIL] = R.drawable.ic_ico_vehicle_subway
        parser[VehicleType.HIGH_SPEED_TRAIN] = R.drawable.ic_ico_vehicle_train
        parser[VehicleType.INTERCITY_BUS] = R.drawable.ic_ico_vehicle_bus
        parser[VehicleType.METRO_RAIL] = R.drawable.ic_ico_vehicle_subway
        parser[VehicleType.MONORAIL] = R.drawable.ic_ico_vehicle_subway
        parser[VehicleType.OTHER] = R.drawable.ic_ico_vehicle_walk
        parser[VehicleType.RAIL] = R.drawable.ic_ico_vehicle_subway
        parser[VehicleType.SHARE_TAXI] = R.drawable.ic_ico_vehicle_car
        parser[VehicleType.SUBWAY] = R.drawable.ic_ico_vehicle_subway
        parser[VehicleType.TRAM] = R.drawable.ic_ico_vehicle_train
        parser[VehicleType.TROLLEYBUS] = R.drawable.ic_ico_vehicle_bus

    }

   fun get(category: VehicleType):Int = parser[category]!!
}