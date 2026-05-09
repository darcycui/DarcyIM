package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.SignedPreKey
import org.springframework.data.jpa.repository.JpaRepository

interface SignedPreKeyRepository: JpaRepository<SignedPreKey, Long> {
    fun findByUserId(userId: Long): SignedPreKey?
}