package com.darcy.kotlin.server.demowebsocket.domain.dto.message

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.domain.table.message.MessageReadStatus
import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil
import org.springframework.data.domain.Page

data class MessageReadStatusDTO(
    val msgId: String = "",
    val userId: Long,
    val conversationType: Int,
    val targetId: Long = 0L,
    val isRead: Boolean = false,
    val readTime: String = "",
    val clientType: String = "",
    val deviceId: String = "",
    val id: Long = 0,
    val createdAt: String = "",
    val updatedAt: String = ""
)

fun MessageReadStatus.toDTO(): MessageReadStatusDTO {
    return MessageReadStatusDTO(
        msgId = this.msgId,
        userId = this.user.id,
        conversationType = this.conversationType.code,
        targetId = this.targetId,
        isRead = this.isRead,
        readTime = TimeUtil.formatDateTimeToString(this.readTime),
        clientType = this.clientType,
        deviceId = this.deviceId,
        id = this.id,
        createdAt = TimeUtil.formatDateTimeToString(this.createdAt),
        updatedAt = TimeUtil.formatDateTimeToString(this.updatedAt)
    )
}

fun List<MessageReadStatus>.toDTO(): List<MessageReadStatusDTO> {
    return this.map { it.toDTO() }
}

fun Page<MessageReadStatus>.toDTO(): Page<MessageReadStatusDTO> {
    return this.map {
        it.toDTO()
    }
}

fun MessageReadStatusDTO.toEntity(user: User): MessageReadStatus {
    return MessageReadStatus(
        msgId = this.msgId,
        user = user,
        conversationType = Conversation.ConversationType.fromCode(this.conversationType),
        targetId = this.targetId,
        isRead = this.isRead,
        readTime = TimeUtil.parseStringToDateTime(this.readTime),
        clientType = this.clientType,
        deviceId = this.deviceId
    ).apply {
    }
}
