package com.darcy.kotlin.server.demowebsocket.domain.error

open class ErrorEntity(
    val timestamp: Long = System.currentTimeMillis(),
    open var status: Int = 0,
    open var error: String = "",
    open var message: String = "",
    open var path: String = "/",
) {
}