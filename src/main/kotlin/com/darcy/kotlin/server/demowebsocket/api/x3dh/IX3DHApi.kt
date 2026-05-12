package com.darcy.kotlin.server.demowebsocket.api.x3dh

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/x3dh")
interface IX3DHApi {
    @PostMapping("/pull/bob/keys")
    fun pullBobKeys(@RequestParam params: Map<String, String>): String
}