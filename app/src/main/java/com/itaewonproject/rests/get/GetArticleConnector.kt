package com.itaewonproject.rests.get

import com.itaewonproject.APIs
import com.itaewonproject.model.sender.Link
import com.itaewonproject.rests.WebResponce

class GetArticleConnector : GetStrategy() {

    override var param = ""
    override val inner: String = "article/"
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
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?"
                },
                {
                    "articleId":2,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp2}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?2"
                },
                {
                    "articleId":3,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp3}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?3"
                },
                {
                    "articleId":4,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp4}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?4"
                },
                {
                    "articleId":5,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp5}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?5"
                },
                {
                    "articleId":6,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp1}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?6"
                },
                {
                    "articleId":7,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp2}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?7"
                },
                {
                    "articleId":8,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp3}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?8"
                },
                {
                    "articleId":9,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp4}",
                    "link":"${link[1]}",
                    "favicon_url":"${ref[1]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?9"
                },
                {
                    "articleId":10,
                    "locationId":1,
                    "customerId":1,
                    "reg_date":null,
                    "image":"${APIs.bmp5}",
                    "link":"${link[0]}",
                    "favicon_url":"${ref[0]}",
                    "summary":"베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요? 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립 베니스에 있는 페기구겐하임 갤러리 근처에 있는 립이 스테이크 맛집 알려드릴까요?10"
                }
            ]
        """.trimIndent()
    }

    override fun get(vararg params: Any): WebResponce {
        val id = params[0] as String
        param = "$id/places"

        var task = Task()
        task.execute()

        return WebResponce(task.get(), statusCode)
    }
}
