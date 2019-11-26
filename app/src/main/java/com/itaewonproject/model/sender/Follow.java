package com.itaewonproject.model.sender;

import java.sql.Timestamp;

public class Follow {
    public long followId;

    public Customer target;

    public Customer follower;

    public boolean follow;

    public Timestamp regDate;

    public Timestamp updateDate;
}
