package com.itaewonproject.model.receiver

import com.itaewonproject.model.sender.Customer

class Messenger{
    var messengerId:Long=0
    var customer = Customer()
    var lastMessage = Message()
}