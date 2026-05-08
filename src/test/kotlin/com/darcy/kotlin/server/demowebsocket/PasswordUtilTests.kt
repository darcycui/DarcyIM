package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.utils.HashUtil
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PasswordUtilTests {

    /**
     * @BeforeAll 和 @AfterAll 需要放在 companion object 中，并且需要 @JvmStatic 注解
     */
    companion object {
        val passwordUtil = PasswordUtil()

        @JvmStatic
        @BeforeAll
        fun beforeAll(): Unit {
            println("beforeAll")
        }

        @JvmStatic
        @AfterAll
        fun afterAll(): Unit {
            println("afterAll")
        }
    }

    /**
     * @BeforeEach 和 @AfterEach 定义在实例对象中
     */
    @BeforeEach
    fun beforeEach(): Unit {
        println("beforeEach")
    }

    @AfterEach
    fun afterEach(): Unit {
        println("afterEach")
    }

    @Test
    fun `test-password-util`() {
        val password = "password"
        val encodedPassword = passwordUtil.encode(password)
        val matches = passwordUtil.matches(password, encodedPassword)
        println("matches: $matches")
        assertEquals(true, matches, "密码匹配失败")
    }

    @Test
    fun `test-password-util-empty`() {
        val password = ""
        val encodedPassword = passwordUtil.encode(password)
        val matches = passwordUtil.matches(password, encodedPassword)
        println("matches: $matches")
        assertEquals(false, matches, "密码匹配失败")
    }

    @Test
    fun `test-password-with-hash256`() {
        val password = "password"
        val passwordHash = HashUtil.sha256Str(password)
        val encodedPassword = passwordUtil.encode(passwordHash)
        val matches = passwordUtil.matches(passwordHash, encodedPassword)
        println("matches: $matches")
        assertEquals(true, matches, "密码匹配失败")
    }
}