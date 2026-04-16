package com.darcy.kotlin.server.demowebsocket.http.global

import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.exception.BaseException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局异常处理
 */
@RestControllerAdvice(annotations = [RestController::class])
class GlobalExceptionHandler {
    // 处理自定义 BaseException 异常
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): String {
        return ResultEntity.error(ex).toJsonString()
    }
    // 处理其他异常 Exception
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): String {
        return ResultEntity.error(BaseException.UNKNOWN_EXCEPTION).toJsonString()
    }
}