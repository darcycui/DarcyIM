package com.darcy.kotlin.server.demowebsocket.domain.dto.x3dh

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.FirstMessage
import java.time.LocalDateTime

/**
 * 离线消息DTO - X3DH密钥交换协议
 */
data class FirstMessageDTO(
    val id: Long = 0,
    val messageId: String = "",
    val userFromId: Long = 0,
    val userToId: Long = 0,
    val senderIdentityKey: String = "",
    val receiverIdentityKey: String = "",
    val ephemeralPublicKey: String = "",
    val usedSignedPreKeyId: Int = 0,
    val usedOneTimePreKeyId: Int? = null,
    val ciphertext: String = "",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

/**
 * Entity 转 DTO（扩展方法）
 */
fun FirstMessage.toDTO(): FirstMessageDTO {
    return FirstMessageDTO(
        id = this.id,
        messageId = this.messageId,
        userFromId = this.userFrom.id,
        userToId = this.userTo.id,
        senderIdentityKey = this.senderIdentityKey,
        receiverIdentityKey = this.receiverIdentityKey,
        ephemeralPublicKey = this.ephemeralPublicKey,
        usedSignedPreKeyId = this.usedSignedPreKeyId,
        usedOneTimePreKeyId = this.usedOneTimePreKeyId,
        ciphertext = this.ciphertext,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

/**
 * DTO 转 Entity（扩展方法）
 * @param userFrom 发送方用户对象
 * @param userTo 接收方用户对象
 */
fun FirstMessageDTO.toEntity(userFrom: User, userTo: User): FirstMessage {
    return FirstMessage(
        userFrom = userFrom,
        userTo = userTo,
        messageId = this.messageId,
        senderIdentityKey = this.senderIdentityKey,
        receiverIdentityKey = this.receiverIdentityKey,
        ephemeralPublicKey = this.ephemeralPublicKey,
        usedSignedPreKeyId = this.usedSignedPreKeyId,
        usedOneTimePreKeyId = this.usedOneTimePreKeyId,
        ciphertext = this.ciphertext
    ).apply {
        if (this@toEntity.id != 0L) {
            this.id = this@toEntity.id
        }
    }
}

/**
 * Entity 列表转 DTO 列表（扩展方法）
 */
fun List<FirstMessage>.toDTOList(): List<FirstMessageDTO> {
    return this.map { it.toDTO() }
}
