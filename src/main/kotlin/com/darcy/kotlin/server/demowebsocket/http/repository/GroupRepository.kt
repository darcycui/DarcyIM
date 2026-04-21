package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.group.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, Long> {
}