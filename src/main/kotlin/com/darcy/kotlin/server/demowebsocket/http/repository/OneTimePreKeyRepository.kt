package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.OneTimePreKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OneTimePreKeyRepository : JpaRepository<OneTimePreKey, Long> {
    fun findByUserId(userId: Long): List<OneTimePreKey>

    @Query("SELECT k FROM OneTimePreKey k WHERE k.user.id = :userId AND " +
            "k.isUsed = FALSE ORDER BY k.id ASC LIMIT 1")
    fun findFirstEnabled(userId: Long): OneTimePreKey?
}