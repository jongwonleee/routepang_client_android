package com.itaewonproject.model.sender;


import com.itaewonproject.maputils.LocationCategory;

import org.locationtech.jts.geom.Point;

public class Location {

    public long locationId=0;

    public String coordinates="";

    public String placeId="";

    public String address="";

    public String name="";

    public double usedTime=0;

    public LocationCategory category=LocationCategory.ACTIVITY;

    public int articleCount=0;

}
