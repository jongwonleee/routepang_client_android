package com.itaewonproject.model.receiver

import com.itaewonproject.model.sender.Customer

class Message{
    var messageId:Long=0
    var text=""
    var regDate:Long=0
    var customer=Customer()
    var isMe=false
 }