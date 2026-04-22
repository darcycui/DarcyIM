package com.darcy.kotlin.server.demowebsocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ConversationTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test-create-conversation`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/conversations/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "15")
                .param("targetId", "16")
                .param("conversationType", "1")
                .param("name", "test")
                .param("avatar", "https://www.baidu.com")
                .param("description", "test")
                .param("settings", "{}")
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-conversations`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/conversations/query/all")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "15")
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-conversation-by-id`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/conversations/query/id")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("conversationId", "5")
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }
}