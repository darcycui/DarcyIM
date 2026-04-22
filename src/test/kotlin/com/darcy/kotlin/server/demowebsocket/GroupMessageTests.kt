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
class GroupMessageTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc


    @Test
    fun `test-send-group-message`(){
        val result = mockMvc.perform(
            post("http://localhost:$port/api/group-messages/send")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "15")
                .param("groupId", "3")
                .param("conversationId", "6")
                .param("content", "新测试群消息4")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-group-messages`(){
        val result = mockMvc.perform(
            post("http://localhost:$port/api/group-messages/query/page")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "16")
                .param("groupId", "3")
                .param("conversationId", "6")
                .param("page", "1")
                .param("size", "2")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

}