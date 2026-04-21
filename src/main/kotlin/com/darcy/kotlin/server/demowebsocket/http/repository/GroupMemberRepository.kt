package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.group.GroupMember
import org.springframework.data.jpa.repository.JpaRepository

interface GroupMemberRepository : JpaRepository<GroupMember, Long> {
    /**
     * 根据 Spring Data JPA 的方法命名规则，findByGroupGroupId 会被解析为：
     * findBy - 查询方法
     * Group - GroupMember 实体中的 group 属性（关联到 Group 实体）
     * GroupId - Group 实体中的 groupId 字段
     */
    fun findByGroupId(groupId: Long): List<GroupMember>
}