package com.darcy.kotlin.server.demowebsocket.domain.table

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

/**
 * 使用JPA注解创建数据库表
 */
@Entity(name = "Message")
open class MessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = -1L,
    open var name: String = ""
)