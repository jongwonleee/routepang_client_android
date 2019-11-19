package com.itaewonproject.rests.get

import com.itaewonproject.rests.WebResponce
import com.itaewonproject.rests.WebStrategy

/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class GetStrategy: WebStrategy() {

    override val method = "GET"

    abstract fun get(vararg params: Any): WebResponce
}
