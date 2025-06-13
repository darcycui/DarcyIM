package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserDatabaseRepository: JpaRepository<UserEntity, Long> {
}