package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/private-messages")
interface IPrivateMessageApi {
    @PostMapping("/send")
    fun sendMessage(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/page")
    fun queryMessagesByConversation(@RequestParam params: Map<String, String>): String
}