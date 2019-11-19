package com.itaewonproject.rests.delete

import com.itaewonproject.rests.WebResponce
import com.itaewonproject.rests.WebStrategy

/* jsonParsing 따로 객체화 시키기 : apiUtils
 * stretegy화. getResult method로 통일
 */
abstract class DeleteStrategy: WebStrategy() {

    override val method = "DELETE"

    abstract fun delete(vararg params: Any): WebResponce
}
