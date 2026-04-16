package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.domain.table.User.UserStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `test-create-user-mockmvc`() {
        val user = User(
            username = "darcy",
            passwordHash = "123456",
            nickname = "Darcy",
            avatar = "https://avatars.githubusercontent.com/u/1020407?v=4",
            phone = "12345678901",
            email = "darcy@gmail.com",
            gender = "male",
            signature = "I'm a developer",
            status = UserStatus.NORMAL,
            lastActiveTime = null,
            deletedAt = null,
            settings = emptyMap(),
            roles = "admin",
            token = "",
        )
        user.createdAt = LocalDateTime.now()
        println("createAt: ${user.createdAt}")
        user.updatedAt = LocalDateTime.now()
        println("updateAt: ${user.updatedAt}")

        val params:  Map<String, String> = mapOf(
        )
        val result = mockMvc.perform(
            post("http://localhost:$port/api/users/create")
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