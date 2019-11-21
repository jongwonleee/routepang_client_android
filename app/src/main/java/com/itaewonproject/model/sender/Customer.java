package com.itaewonproject.model.sender;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Customer implements Serializable {

    public long customerId;

    public String account;

    public String password;

    public String reference;

    public String email;

    @Override
    public boolean equals(@Nullable Object obj) {
        try{
            return ((Customer)obj).customerId == customerId;
        }catch (ClassCastException e){
            return false;
        }
    }
}
