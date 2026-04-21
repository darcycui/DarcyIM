package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/groups")
interface IGroupApi {
    @PostMapping("/create")
    fun createGroup(@RequestParam params: Map<String, String>): String

    @PostMapping("/update")
    fun updateGroup(@RequestParam params: Map<String, String>): String

    @PostMapping("/delete")
    fun deleteGroup(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/id")
    fun queryGroupById(@RequestParam params: Map<String, String>): String

    @PostMapping("/invite")
    fun inviteToGroup(@RequestParam params: Map<String, String>): String

    @PostMapping("/query/members")
    fun queryGroupMembers(@RequestParam params: Map<String, String>): String


}