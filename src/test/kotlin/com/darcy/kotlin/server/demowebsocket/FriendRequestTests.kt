package com.darcy.kotlin.server.demowebsocket

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FriendRequestTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `test-create-friend-request`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/friend-requests/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fromUserPhone", "150999888777")
                .param("toUserPhone", "138000111222")
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-friend-request-by-from-user-phone`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/friend-requests/query/from")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fromUserId", "13")

        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-friend-request-by-to-user-phone`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/friend-requests/query/to")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("toUserId", "14")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-accept-friend-request`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/friend-requests/accept")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("friendRequestId", "1")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-reject-friend-request`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/friend-requests/reject")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("friendRequestId", "1")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }
}