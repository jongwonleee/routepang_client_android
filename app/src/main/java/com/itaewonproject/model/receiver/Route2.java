package com.itaewonproject.model.receiver;


import java.sql.Timestamp;
import java.util.List;

public class Route2 {
    public long routeId;

    public RouteType type;

    public String title;

    public String boundary;

    public Long startDate;

    public Long endDate;

    public long parentId;

    public long customerId;

    public Long regDate;

    public List<Route2> routes;

}
