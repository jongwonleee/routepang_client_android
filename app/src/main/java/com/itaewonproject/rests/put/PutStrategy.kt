package com.itaewonproject.rests.put

import com.itaewonproject.rests.WebResponce
import com.itaewonproject.rests.WebStrategy

/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class PutStrategy: WebStrategy() {

    override val method = "PUT"

    abstract fun put(vararg params: Any): WebResponce
}
