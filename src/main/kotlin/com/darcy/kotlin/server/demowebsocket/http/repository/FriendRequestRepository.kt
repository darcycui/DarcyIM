package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.FriendRequest
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRequestRepository : JpaRepository<FriendRequest, Long> {
    // 我发起的好友请求
    fun findByFromUserId(fromUserId: Long): List<FriendRequest>

    // 别人发给我的好友请求
    fun findByToUserId(toUserId: Long): List<FriendRequest>

    // 更新，用于接受、拒绝、忽略好友请求——再次调用save()

}