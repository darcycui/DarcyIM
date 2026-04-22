package com.darcy.kotlin.server.demowebsocket

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
class PrivateMessageTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    private var count = 1
    private val conversationId = "2"
    private val senderId = "15"
    private val receiverId = "16"
//    private val senderId = "16"
//    private val receiverId = "15"

    @Test
    fun `test-send-message`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/private-messages/send")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("senderId", senderId)
                .param("receiverId", receiverId)
                .param("content", "新文本消息:$count")
                .param("conversationId", conversationId)
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-messages-by-conversation`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/private-messages/query/page")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("conversationId", conversationId)
                .param("page", "0")
                .param("size", "2")
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }
}