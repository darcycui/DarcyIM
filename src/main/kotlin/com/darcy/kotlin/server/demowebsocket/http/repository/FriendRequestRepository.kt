package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.FriendRequest
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FriendRequestRepository : JpaRepository<FriendRequest, Long> {
    // 我发起的好友请求
    @Query("select f from FriendRequest f where f.fromUser.id = :fromUserId")
    fun queryByFromUserId(fromUserId: Long): List<FriendRequest>

    // 别人发给我的好友请求
    @Query("select f from FriendRequest f where f.toUser.id = :toUserId")
    fun queryByToUserId(toUserId: String): List<FriendRequest>

    // 更新，用于接受、拒绝、忽略好友请求——再次调用save()

}