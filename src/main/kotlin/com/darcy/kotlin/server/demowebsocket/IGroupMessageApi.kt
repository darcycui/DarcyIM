package com.darcy.kotlin.server.demowebsocket

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/group-messages")
interface IGroupMessageApi {
    @PostMapping("/send")
    fun sendMessage(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/page")
    fun queryGroupMessages(@RequestParam params: Map<String, String>): String
}