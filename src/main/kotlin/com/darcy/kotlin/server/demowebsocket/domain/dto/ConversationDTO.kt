package com.darcy.kotlin.server.demowebsocket.domain.dto

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil
import org.springframework.data.domain.Page

data class ConversationDTO(
    val conversationId: String = "",
    val userId: Long,
    val conversationType: Int,
    val targetId: Long = 0L,
    val lastMsgId: String = "",
    val lastMsgContent: String = "",
    val lastMsgType: Int = 0,
    val lastMsgSenderId: Long = 0L,
    val lastMsgTime: String = "",
    val unreadCount: Int = 0,
    val isMuted: Boolean = false,
    val isPinned: Boolean = false,
    val isTop: Boolean = false,
    val draft: String = "",
    val draftTime: String = "",
    val extData: Map<String, Any> = emptyMap(),
    val id: Long = 0,
    val createdAt: String = "",
    val updatedAt: String = ""
)

fun Conversation.toDTO(): ConversationDTO {
    return ConversationDTO(
        conversationId = this.conversationId,
        userId = this.user.id,
        conversationType = this.conversationType.code,
        targetId = this.targetId,
        lastMsgId = this.lastMsgId,
        lastMsgContent = this.lastMsgContent,
        lastMsgType = this.lastMsgType,
        lastMsgSenderId = this.lastMsgSenderId,
        lastMsgTime = TimeUtil.formatDateTimeToString(this.lastMsgTime),
        unreadCount = this.unreadCount,
        isMuted = this.isMuted,
        isPinned = this.isPinned,
        isTop = this.isTop,
        draft = this.draft,
        draftTime = TimeUtil.formatDateTimeToString(this.draftTime),
        extData = this.extData,
        id = this.id,
        createdAt = TimeUtil.formatDateTimeToString(this.createdAt),
        updatedAt = TimeUtil.formatDateTimeToString(this.updatedAt)
    )
}

fun List<Conversation>.toDTO(): List<ConversationDTO> {
    return this.map { it.toDTO() }
}

fun Page<Conversation>.toDTO(): Page<ConversationDTO> {
    return this.map { it.toDTO() }
}

fun ConversationDTO.toEntity(user: User): Conversation {
    return Conversation(
        conversationId = this.conversationId,
        user = user,
        conversationType = Conversation.ConversationType.fromCode(this.conversationType),
        targetId = this.targetId,
        lastMsgId = this.lastMsgId,
        lastMsgContent = this.lastMsgContent,
        lastMsgType = this.lastMsgType,
        lastMsgSenderId = this.lastMsgSenderId,
        lastMsgTime = TimeUtil.parseStringToDateTime(this.lastMsgTime),
        unreadCount = this.unreadCount,
        isMuted = this.isMuted,
        isPinned = this.isPinned,
        isTop = this.isTop,
        draft = this.draft,
        draftTime = TimeUtil.parseStringToDateTime(this.draftTime),
        extData = this.extData,
    ).apply {
    }
}
