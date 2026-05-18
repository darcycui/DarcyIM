package com.darcy.kotlin.server.demowebsocket.utils

object TokenUtil {
    private const val TAG = "TokenUtil"
    const val TOKEN_HEADER= "Authorization"
    const val BEARER_TOKEN_PREFIX = "Bearer "
    fun cutOnlyToken(bearerToken: String): String {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return bearerToken
    }
}