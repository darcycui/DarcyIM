package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.Friendship
import org.springframework.data.jpa.repository.JpaRepository

interface FriendshipRepository : JpaRepository<Friendship, Long> {
    fun findByUserId(userId: Long): List<Friendship>

    fun findByUserIdAndFriendId(userId: Long, friendId: Long): Friendship?
}