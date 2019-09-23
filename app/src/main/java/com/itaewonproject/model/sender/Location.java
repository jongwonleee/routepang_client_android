package com.itaewonproject.model.sender;


import com.itaewonproject.LocationCategory;

import org.locationtech.jts.geom.Point;

public class Location {

    public long locationId;

    public Point coordinates;

    public String placeId;

    public String address;

    public String name;

    public double used;

    public LocationCategory category;

    public int articleCount;

}
