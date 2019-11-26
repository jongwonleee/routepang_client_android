package com.itaewonproject.model.receiver

import com.itaewonproject.model.sender.Customer
import java.sql.Timestamp

class ChatMessage{
    var Id:Long=0
    var senderId:Long=0
    var receiverId:Long=0
    var content=""
    var regDate:Long= 0
 }