package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.config.JwtToken.JWT_TOKEN
import com.darcy.kotlin.server.demowebsocket.config.jwt.JwtTokenProvider
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JWTTests {
    @Test
    fun `test-jwt-generate-token`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token = jwtTokenProvider.generateToken("darcy")
        println(token)
    }

    @Test
    fun `test-jwt-validate-token`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token = jwtTokenProvider.generateToken("darcy")
        println(token)
        val isValid = jwtTokenProvider.validateToken(token)
        assertEquals(true, isValid, "token验证失败")
    }

    @Test
    fun `test-jwt-validate-token-fixed`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token = JWT_TOKEN
        println(token)
        val isValid = jwtTokenProvider.validateToken(token, 1)
        assertEquals(true, isValid, "token验证失败")
    }


    @Test
    fun `test-jwt-get-username-from-jwt`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token = jwtTokenProvider.generateToken("darcy")
        println(token)
        val username = jwtTokenProvider.getUsernameFromJWT(token)
        println(username)
        assertEquals("darcy", username, "username验证失败")
    }

    /**
     * 使用 Token 版本控制方案，实现旧Token失效：
     * 1.在 User 表中添加一个 tokenVersion 字段
     * 2.生成 token 时将版本号嵌入到 JWT 中
     * 3.验证 token 时检查版本号是否与数据库中的一致
     * 4.每次生成新 token 时，版本号+1，旧 token 自然失效
     */
    @Test
    fun `test-jwt-expired-by-refresh`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token1 = jwtTokenProvider.generateToken("darcy", 1)
        println("token1=$token1")
        val isValid1 = jwtTokenProvider.validateToken(token1, 1)
        assertEquals(true, isValid1, "token验证失败")

        val token2 = jwtTokenProvider.generateToken("darcy", 2)
        println("token2=$token2")
        val isValid11 = jwtTokenProvider.validateToken(token1, 1)
        assertEquals(true, isValid11, "token验证失败")
        val isValid111 = jwtTokenProvider.validateToken(token1, 2)
        assertEquals(false, isValid111, "token验证失败")
        val isValid2 = jwtTokenProvider.validateToken(token2, 2)
        assertEquals(true, isValid2, "token验证失败")

    }

    @Test
    fun `test-jwt-token-version`() {
        val jwtTokenProvider = JwtTokenProvider()
        val version0 = jwtTokenProvider.getTokenVersion("")
        assertEquals(0, version0, "token版本号验证失败")
        val token1 = jwtTokenProvider.generateToken("darcy", 1)
        val version1 = jwtTokenProvider.getTokenVersion(token1)
        assertEquals(1, version1, "token版本号验证失败")
        println("token1=$token1")
        val token2 = jwtTokenProvider.generateToken("darcy", 2)
        println("token2=$token2")
        val version2 = jwtTokenProvider.getTokenVersion(token2)
        assertEquals(2, version2, "token版本号验证失败")
        val token3 = jwtTokenProvider.generateToken("darcy", 3)
        println("token3=$token3")
        val version3 = jwtTokenProvider.getTokenVersion(token3)
        assertEquals(3, version3, "token版本号验证失败")
    }

    @Test
    fun `test-jwt-token-expired-by-time`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token1 = jwtTokenProvider.generateToken("darcy", 1, 1000)
        println("token1=$token1")
        val isValid1 = jwtTokenProvider.validateToken(token1, 1)
        assertEquals(true, isValid1, "token验证失败")
        Thread.sleep(2000)
        val isValid2 = jwtTokenProvider.validateToken(token1, 1)
        assertEquals(false, isValid2, "token验证失败")
    }
}