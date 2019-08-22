package com.itaewonproject.ServerResult


import com.itaewonproject.ServerModel.Link
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