package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/group-invites")
interface IGroupInviteApi {
    @PostMapping("/create")
    fun createGroupInvite(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/from")
    fun queryGroupInviteByFromUser(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/to")
    fun queryGroupInviteByToUser(@RequestParam params: Map<String, String>): String

}