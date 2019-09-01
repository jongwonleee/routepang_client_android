package com.itaewonproject.model.receiver

import kotlin.collections.ArrayList

class Folder(override var title: String,
             override var location: String,
             override var id: Int,
             var routes:ArrayList<Route>
):RouteListBase{
    var opened=false
    override var type = 1
    override lateinit var date: String
    override var parIndex = -1
    init{
        //if (title.equals("")) title = routes[0].title
        var old = routes[0].time
        var new = routes[0].time
        for(r in routes){
            if(old.after(r.time)) old = r.time
            if(new.before(r.time)) new = r.time
        }
        if(old.equals(new)){
            date = getDate(new)
        }else
        {
            date = getDate(old) +" ~\n "+getDate(new)
        }
    }
}