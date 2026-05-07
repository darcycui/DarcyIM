package com.darcy.kotlin.server.demowebsocket.config.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {
    companion object {
        // 密钥长度 与 使用算法匹配 512
        private const val JWT_SECRET: String = "jwtJWT1234567890jwtJWT1234567890jwtJWT1234567890jwtJWT1234567890"
        private const val JWT_EXPIRATION: Long = 604800000L // 7 days
        private const val TOKEN_VERSION_CLAIM = "tokenVersion"
    }

    // 生成 Key
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())
    }

    /**
     * 生成 JWT token
     */
    fun generateToken(username: String, tokenVersion: Int = 0, expiration: Long = JWT_EXPIRATION): String {
        return kotlin.runCatching {
            val now: Date = Date()
            val expiryDate: Date = Date(now.time + expiration)
            Jwts.builder()
                .subject(username)
                .claim(TOKEN_VERSION_CLAIM, tokenVersion)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS512)
                .compact()
        }.onFailure {
            it.printStackTrace()
        }.getOrElse { "" }
    }

    /**
     * 验证 JWT token
     */
    fun validateToken(token: String, currentVersion: Int = 0): Boolean {
        if (token.isEmpty()) {
            return false
        }
        return kotlin.runCatching {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            val tokenVersion = when (val tokenVersionObj = claims.payload[TOKEN_VERSION_CLAIM]) {
                is Number -> tokenVersionObj.toInt()
                else -> 0
            }
            println("tokenVersion: $tokenVersion currentVersion: $currentVersion")
            return tokenVersion == currentVersion
        }.onFailure {
            it.printStackTrace()
            return false
        }.getOrElse { false }
    }

    /**
     * 从 JWT token 获取用户名
     */
    fun getUsernameFromJWT(token: String): String {
        if (token.isEmpty()) {
            return ""
        }
        return kotlin.runCatching {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        }.onFailure {
            it.printStackTrace()
        }.getOrElse { "" }
    }

    /**
     * 从 JWT token 获取 token 版本
     */
    fun getTokenVersion(token: String): Int {
        if (token.isEmpty()) {
            return 0
        }
        return kotlin.runCatching {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)

            when (val versionObj = claims.payload[TOKEN_VERSION_CLAIM]) {
                is Number -> versionObj.toInt()
                else -> 0
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrElse { 0 }
    }
}