package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api")
interface ILoginApi {
    @PostMapping("/login")
    fun login(@RequestParam params: Map<String, String>): String
}