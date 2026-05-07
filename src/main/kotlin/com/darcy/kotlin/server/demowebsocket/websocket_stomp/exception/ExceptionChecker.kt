package com.darcy.kotlin.server.demowebsocket.websocket_stomp.exception

import java.io.IOException

object ExceptionChecker {

    fun isIgnorableException(exception: Throwable?): Boolean {
        if (exception == null) return false
        val message = exception.message ?: return false
        // 判断是否为可忽略的异常消息
        return message.contains("session is closed", ignoreCase = true) ||
                message.contains("Cannot send a message", ignoreCase = true) ||
                message.contains("connection is closed", ignoreCase = true) ||
                message.contains("broken pipe", ignoreCase = true) ||
                message.contains("Connection reset", ignoreCase = true) ||
                message.contains("已关闭的会话") ||
                exception is IOException ||
                exception is java.lang.IllegalStateException ||
                (exception.cause != null && isIgnorableException(exception.cause))
    }
}