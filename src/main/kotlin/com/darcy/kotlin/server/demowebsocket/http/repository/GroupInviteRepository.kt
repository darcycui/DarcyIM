package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.group.GroupInvite
import org.springframework.data.jpa.repository.JpaRepository

interface GroupInviteRepository : JpaRepository<GroupInvite, Long> {

    fun findByInviterId(inviterId: Long): List<GroupInvite>

    fun findByInviteeId(inviteeId: Long): List<GroupInvite>
}