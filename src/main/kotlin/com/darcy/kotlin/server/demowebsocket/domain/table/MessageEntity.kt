package com.darcy.kotlin.server.demowebsocket.domain.table

import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil
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

    open val fromUser: String = "",
    open val toUser: String = "",
    open val text: String = "",
    open val recipient: String = "",
    open val time: String? = TimeUtil.getCurrentTimeStamp(),
) {
    override fun toString(): String {
        return "MessageEntity(id=$id, from='$fromUser', text='$text', recipient='$recipient', time=$time)"
    }
}