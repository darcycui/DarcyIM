package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/conversations")
interface IConversationApi {
    @PostMapping("/create")
    fun createConversation(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/all")
    fun queryConversations(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/id")
    fun queryConversationById(@RequestParam params: Map<String, String>): String
}