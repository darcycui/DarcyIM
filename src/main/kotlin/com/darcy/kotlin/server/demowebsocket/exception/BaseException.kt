package com.darcy.kotlin.server.demowebsocket.exception

import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity

open class BaseException(
    val exceptionCode: Int = -1,
    val exceptionMessage: String
) : IllegalStateException("") {
    companion object {
        val COMMON_EXCEPTION = BaseException(101, "未知异常")
    }
}

fun BaseException.toJsonString(): String {
    return ResultEntity.error<String>(this.exceptionCode, this.exceptionMessage).toJsonString()
}