package com.darcy.kotlin.server.demowebsocket.websocket_stomp.interceptor

import java.security.Principal

/**
 * stomp 用户唯一标识 username
 */
class MyPrincipal(private val name: String) : Principal {
    override fun getName(): String {
        return name
    }
}