package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.OneTimePreKey
import org.springframework.data.jpa.repository.JpaRepository

interface OneTimePreKeyRepository: JpaRepository<OneTimePreKey, Long> {
    fun findByUserId(userId: Long): List<OneTimePreKey>
}