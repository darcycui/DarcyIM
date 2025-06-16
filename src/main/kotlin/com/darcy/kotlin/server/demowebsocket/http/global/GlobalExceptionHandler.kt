package com.darcy.kotlin.server.demowebsocket.http.global

import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.exception.BaseException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(annotations = [RestController::class])
class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleFileNotFound(ex: BaseException): String {
        return ResultEntity.error<String>(999, ex.message ?: "Unknown error.").toJsonString()
    }
}