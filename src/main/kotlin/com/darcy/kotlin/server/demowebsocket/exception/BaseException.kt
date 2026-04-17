package com.darcy.kotlin.server.demowebsocket.exception

import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity

open class BaseException(
    val exceptionCode: Int = -1,
    var exceptionMessage: String
) : IllegalStateException("exceptionCode=$exceptionCode exceptionMessage=$exceptionMessage") {
    companion object {
        val UNKNOWN_EXCEPTION = BaseException(101, "未知异常")
    }
}

fun BaseException.toJsonString(): String {
    return ResultEntity.error(this).toJsonString()
}