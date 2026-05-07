package com.darcy.kotlin.server.demowebsocket.config

object JwtToken {
    const val HEADER_AUTHORIZATION = "Authorization"
    private const val TOKEN_PREFIX = "Bearer "
    private const val TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUb20iLCJ0b2tlblZlcnNpb24iOjQsImlhdCI6MTc3ODEzNTk4NiwiZXhwIjoxNzc4NzQwNzg2fQ.QnDTYae2wD9Gefkex6KnihjuzBPzsqV46KLIs2e4L2kFNytWQl3jH_mCC3i6oipk_9sB7jK2u_i-CZsOTXgv4Q"
    const val JWT_TOKEN = "$TOKEN_PREFIX$TOKEN"
}