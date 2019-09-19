package com.itaewonproject.player

import com.google.gson.Gson
import com.itaewonproject.APIs
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.sender.Link

class ArticleConnector : WebConnectStrategy() {

    override var param = ""
    override var method: String = "GET"
    override var inner: String = "article/getArticleByLocationId/"
    override lateinit var mockData: String
    init {
        val ref = listOf<String>("https://facebookbrand.com/wp-content/themes/fb-branding/assets/images/fb-logo.png?v2",
            "https://instagram-brand.com/wp-content/uploads/2016/11/Instagram_AppIcon_Aug2017.png?w=300")
        val l1 = Link()
        val l2 = Link()
        l1.linkUrl = "http://www.instagram.com"
        l2.linkUrl = "http://www.facebook.com"
        val link = listOf(l2, l1)
        mockData = """
            [
                {
                    "articleId":1,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp1}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle1"
                },
                {
                    "articleId":2,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp2}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle2"
                },
                {
                    "articleId":3,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp3}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle3"
                },
                {
                    "articleId":4,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp4}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle4"
                },
                {
                    "articleId":5,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp5}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle5"
                },
                {
                    "articleId":6,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp1}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle6"
                },
                {
                    "articleId":7,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp2}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle7"
                },
                {
                    "articleId":8,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp3}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle8"
                },
                {
                    "articleId":9,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp4}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"summary\nof\nArticle9"
                },
                {
                    "articleId":10,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp5}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"summary\nof\nArticle10"
                }
            ]
        """.trimIndent()
    }

    override fun get(vararg params: Any): String {
        val id = params[0] as Long
        param = "$id"

        var task = Task()
        task.execute()

        return task.get()
    }
/*


    fun getByLocationId(id:Long):ArrayList<Article>{
        param = "$id"

        var task = Task()
        task.execute()

        var result = task.get()


        return JsonParser().listJsonParsing(result,Article::class.java)
    }
*/

    fun postByCustomerId(article: com.itaewonproject.model.sender.Article) {
        method = "POST"
        inner = "article/postArticle/"
        param = "customer_id=${article.customerId}&&link_id=${article.link.linkId}"
        article.customerId = 1
        var task = Task()
        task.execute(Gson().toJson(article))
        // task.get()
        /*var result = task.get()
        var arr= locationJsonParsing(result)
        var ret = ArrayList<com.itaewonproject.ServerResult.Location>()*/
    }
}
