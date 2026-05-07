package com.darcy.kotlin.server.demowebsocket.config

object JwtToken {
    const val HEADER_AUTHORIZATION = "Authorization"
    private const val TOKEN_PREFIX = "Bearer "
    private const val TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUb20iLCJ0b2tlblZlcnNpb24iOjUsImlhdCI6MTc3ODEzNzI5MiwiZXhwIjoxNzc4NzQyMDkyfQ.edqYM6zZAiSPt7uH8SnMiV7cswkZYfoVc_aHCd5nJAqTZWiDdM_cJgL_Z3AsHzrB957C9z1TEYVx-GZicMohDg"
    const val JWT_TOKEN = "$TOKEN_PREFIX$TOKEN"
}