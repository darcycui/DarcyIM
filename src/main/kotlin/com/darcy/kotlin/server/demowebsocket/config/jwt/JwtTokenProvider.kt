package com.darcy.kotlin.server.demowebsocket.config.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {
    // 密钥长度 与 使用算法匹配 512
    private val JWT_SECRET: String = "jwtJWT1234567890jwtJWT1234567890jwtJWT1234567890jwtJWT1234567890"
    private val JWT_EXPIRATION: Long = 604800000L // 7 days

    // 生成 Key
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())
    }

    // 生成 JWT token
    fun generateToken(username: String): String {
        val now: Date = Date()
        val expiryDate: Date = Date(now.time + JWT_EXPIRATION)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    // 验证 JWT token
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    // 从 JWT token 获取用户名
    fun getUsernameFromJWT(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }
}