package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.IdentityKey
import org.springframework.data.jpa.repository.JpaRepository

interface IdentityKeyRepository : JpaRepository<IdentityKey, Long> {
    fun findByUserId(userId: Long): IdentityKey?
}