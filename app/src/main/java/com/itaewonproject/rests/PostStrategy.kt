package com.itaewonproject.rests


/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class PostStrategy: WebStrategy() {

    override val method = "POST"

    abstract fun post(vararg params: Any): WebResponce


}
