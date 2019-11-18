package com.itaewonproject.model.sender;


import com.itaewonproject.maputils.LocationCategory;

import org.locationtech.jts.geom.Point;

public class Location {

    public long locationId;

    public String coordinates;

    public String placeId;

    public String address;

    public String name;

    public double usedTime;

    public LocationCategory category;

    public int articleCount;

}
