package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/friend-requests")
interface IFriendRequestApi {
    @PostMapping("/create")
    fun createFriendRequest(@RequestParam params: Map<String, String>): String

    @PostMapping("/accept")
    fun acceptFriendRequest(@RequestParam params: Map<String, String>): String

    @PostMapping("/reject")
    fun rejectFriendRequest(@RequestParam params: Map<String, String>): String

    @PostMapping("/ignore")
    fun ignoreFriendRequest(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/from")
    fun queryFriendRequestByFromUser(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/to")
    fun queryFriendRequestByToUser(@RequestParam params: Map<String, String>): String
}
