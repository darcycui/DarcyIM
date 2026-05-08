package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.utils.HashUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LoginTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test-login`(){
        val result = mockMvc.perform(
            post("http://localhost:$port/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Snow White") // id:20 及以后的用户密码需要hash后传递
                .param("phone", "152000111222")
                .param("password", HashUtil.sha256Str("123456"))
//                .param("password", "123456")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-login-jerry`(){
        val result = mockMvc.perform(
            post("http://localhost:$port/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Jerry")
                .param("phone", "138000111222")
                .param("password", "123456")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }
}