package com.itaewonproject.rests

class WebResponce {
    var body:String?=null
    var responceCode:Int?=null
    constructor(body:String?,responceCode:Int?){
        this.body=body
        this.responceCode=responceCode
    }
}