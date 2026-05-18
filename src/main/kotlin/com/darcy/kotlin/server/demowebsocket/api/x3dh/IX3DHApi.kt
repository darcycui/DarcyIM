package com.darcy.kotlin.server.demowebsocket.api.x3dh

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/x3dh")
interface IX3DHApi {
    @PostMapping("/push/keys")
    fun pushKeys(@RequestParam params: Map<String, String>): String

    @PostMapping("/pull/keys")
    fun pullKeys(@RequestParam params: Map<String, String>): String

    @PostMapping("/push/alice/hello")
    fun pushHelloMessage(@RequestParam params: Map<String, String>): String

    @PostMapping("/pull/alice/hello")
    fun pullAliceHello(@RequestParam params: Map<String, String>): String
}