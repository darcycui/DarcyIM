package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {
    @Query("select u from User u where u.username = :name and u.password = :password ")
    fun findByNameAndPassword(name: String, password: String): UserEntity?

    @Query("select u from User u where u.username = :name")
    fun findByName(name: String): UserEntity?
}