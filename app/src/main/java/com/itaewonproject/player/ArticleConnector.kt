package com.itaewonproject.player

import com.google.gson.Gson
import com.itaewonproject.WebConnectStrategy
import com.itaewonproject.JsonParser
import com.itaewonproject.model.receiver.Article

class ArticleConnector: WebConnectStrategy() {
    override var param=""
    override var method: String = "GET"
    override var inner: String ="article/getArticleByLocationId/"

    fun getByLocationId(id:Long):ArrayList<Article>{
        param = "$id"

        var task = Task()
        task.execute()

        var result = task.get()


        return JsonParser().listJsonParsing(result,Article::class.java)
    }

    fun postByCustomerId(article: com.itaewonproject.model.sender.Article){
        method= "POST"
        inner = "article/postArticle/"
        param = "customer_id=${article.customerId}&&link_id=${article.link.linkId}"
        article.customerId=1
        var task = Task()
        task.execute(Gson().toJson(article))
        //task.get()
        /*var result = task.get()
        var arr= locationJsonParsing(result)
        var ret = ArrayList<com.itaewonproject.ServerResult.Location>()*/

    }

}