package com.itaewonproject.model.receiver

import java.sql.Timestamp

class Newsfeed {
    var customerId: Long = 0
    var eventId: Long = 0
    var eventType: NewsfeedCategory = NewsfeedCategory.ARTICLE
    var jsonData: String = ""
    var regDate: Timestamp? = null
    var updateDate: Timestamp? = null
}

enum class NewsfeedCategory {
    ROUTE, ARTICLE, LOCATION
}