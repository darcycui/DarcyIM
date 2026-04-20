package com.darcy.kotlin.server.demowebsocket.domain.dto.message

import com.darcy.kotlin.server.demowebsocket.domain.table.message.PrivateMessage
import java.time.LocalDateTime

data class PrivateMessageDTO(
    val msgId: String = "",
    val senderId: Long = 0,
    val senderName: String = "",
    val receiverId: Long = 0,
    val receiverName: String = "",
    val content: String = "",
    val msgType: String = "TEXT",
    val isRead: Boolean = false,
    val isRecalled: Boolean = false
)

fun PrivateMessage.toDTO(): PrivateMessageDTO {
    return PrivateMessageDTO(
        msgId = this.msgId,
        senderId = this.sender.id,
        senderName = this.sender.username,
        receiverId = this.receiver.id,
        receiverName = this.receiver.username,
        content = this.content,
        msgType = this.msgType.name,
        isRead = this.isRead,
        isRecalled = this.isRecalled
    )
}

fun PrivateMessageDTO.toEntity(
    sender: com.darcy.kotlin.server.demowebsocket.domain.table.User,
    receiver: com.darcy.kotlin.server.demowebsocket.domain.table.User
): PrivateMessage {
    return PrivateMessage(
        msgId = this.msgId.ifEmpty { java.util.UUID.randomUUID().toString() },
        sender = sender,
        receiver = receiver,
        content = this.content,
        msgType = PrivateMessage.MessageType.valueOf(this.msgType),
    )
}
