package com.darcy.kotlin.server.demowebsocket.utils

object TokenUtil {
    fun cutOnlyToken(bearerToken: String): String {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return bearerToken
    }
}