package com.itaewonproject.model.receiver;

public class Product {
    public long productId;
    public Location location;

    public com.itaewonproject.model.sender.Product toSenderModel(){
        com.itaewonproject.model.sender.Product p = new com.itaewonproject.model.sender.Product();
        p.location = location.getServerModel();
        p.productId=productId;
        return p;
    }
}
