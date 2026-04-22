package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.message.GroupMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GroupMessageRepository:JpaRepository<GroupMessage, Long> {

    @Query(
        "SELECT gm FROM GroupMessage gm WHERE gm.group.id = :groupId ORDER BY gm.sendTime DESC"
    )
    fun findGroupMessagePage(groupId: Long, pageable: Pageable): Page<GroupMessage>
}