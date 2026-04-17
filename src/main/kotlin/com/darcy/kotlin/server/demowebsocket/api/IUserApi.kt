package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api/users")
interface IUserApi {

    @PostMapping("/create")
    fun createUser(@RequestParam params: Map<String, String>): String

    @PostMapping("/update")
    fun updateUser(@RequestParam params: Map<String, String>): String

    @PostMapping("/delete")
    fun deleteUser(@RequestParam params: Map<String, String>): String

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): String

    @GetMapping("/all")
    fun getAllUsers(): String
}