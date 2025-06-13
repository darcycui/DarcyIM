package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.Objects

@RequestMapping("/api/users")
interface IUserApi {

    @PostMapping("/create")
    fun createUser(@RequestParam params: Map<String, Any>): String

    @PostMapping("/update")
    fun updateUser(@RequestParam params: Map<String, Any>): String

    @PostMapping("/delete")
    fun deleteUser(@RequestParam params: Map<String, Any>): String

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): String

    @GetMapping("/all")
    fun getAllUsers(): String
}