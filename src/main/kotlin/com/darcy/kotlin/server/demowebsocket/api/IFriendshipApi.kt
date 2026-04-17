package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/friendships")
interface IFriendshipApi {
    @PostMapping("/query")
    fun queryFriendships(@RequestParam params: Map<String, Any>): String
}