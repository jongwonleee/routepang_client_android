package com.itaewonproject.model.sender;

import java.util.ArrayList;
import java.util.List;

public class Product {

    /**
     *  id
     */
    public long productId;

    /**
     *  연결된 사용자 장바구니
     */
    public Basket basket = new Basket();

    /**
     *  location
     */
    public Location location;

    /**
     *  어떤 루트에 포함된 item인지
     */

    public List<Route> route = new ArrayList<>();
}

