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
class GroupTest {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test-create-group`(){
        val result = mockMvc.perform(
            post("http://localhost:$port/api/groups/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("ownerId", "15")
                .param("groupName", "group3")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-group-by-id`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/groups/query/id")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("groupId", "3")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-query-group-members`() {
        val result = mockMvc.perform(
            post("http://localhost:$port/api/groups/query/members")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("groupId", "3")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }

    @Test
    fun `test-invite-user-to-group`(){
        val result = mockMvc.perform(
            post("http://localhost:$port/api/groups/invite")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("inviterId", "15")
                .param("inviteeId", "16")
                .param("groupId", "3")
        ).andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
        println("result-->$result")
    }
}