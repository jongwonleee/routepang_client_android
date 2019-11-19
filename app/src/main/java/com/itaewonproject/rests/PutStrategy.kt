package com.itaewonproject.rests

/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class PutStrategy: WebStrategy() {

    override val method = "PUT"

    abstract fun put(vararg params: Any): WebResponce
}
