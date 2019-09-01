package com.itaewonproject.model.receiver


import com.itaewonproject.model.sender.Link
import java.sql.Timestamp


class Article {

    var articleId: Long = 0

    var locationId: Long = 0

    var customerId: Long = 0

    var image: String=""

    var summary: String?=""

    var link: Link=Link()

    var favicon_url: String=""

    var reg_date: Timestamp? = null

}