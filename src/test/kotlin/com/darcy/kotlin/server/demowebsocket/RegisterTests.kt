package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.domain.table.User.UserStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RegisterTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    /**
     * u/1020407 (你原本使用的)
     * u/1
     * u/2
     * u/9919
     * u/583231
     * u/1726002
     * u/23617146
     * u/3814078
     * u/44036562
     * u/66685688
     */
    private val user1 = User(
        username = "Butch",
        passwordHash = "123456",
        nickname = "ButchButch",
        avatar = "https://avatars.githubusercontent.com/u/3814078?v=4",
        phone = "151999888777",
        email = "Butch@gmail.com",
        gender = "male",
        signature = "I'm a black cat",
        status = UserStatus.NORMAL,
        lastActiveTime = null,
        deletedAt = null,
        settings = emptyMap(),
        roles = "admin",
        token = "",
    )

    @Test
    fun `test-register`() {
        val user = user1
        user.createdAt = LocalDateTime.now()
        println("createAt: ${user.createdAt}")
        user.updatedAt = LocalDateTime.now()
        println("updateAt: ${user.updatedAt}")

        val params: Map<String, String> = mapOf(
        )
        val result = mockMvc.perform(
            post("http://localhost:$port/api/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", user.username)
                .param("password", user.passwordHash)
                .param("nickname", user.nickname)
                .param("avatar", user.avatar)
                .param("phone", user.phone)
                .param("email", user.email)
                .param("gender", user.gender)
                .param("signature", user.signature)
                .param("status", user.status.name)
                .param("lastActiveTime", user.lastActiveTime.toString())
                .param("deletedAt", user.deletedAt.toString())
                .param("settings", objectMapper.writeValueAsString(user.settings))
                .param("roles", user.roles)
                .param("token", user.token)
                .param("createdAt", user.createdAt.toString())
                .param("updatedAt", user.updatedAt.toString())
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }
}