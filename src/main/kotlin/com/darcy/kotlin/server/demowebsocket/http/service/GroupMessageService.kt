package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.domain.table.message.GroupMessage
import com.darcy.kotlin.server.demowebsocket.exception.ConversationException
import com.darcy.kotlin.server.demowebsocket.exception.GroupException
import com.darcy.kotlin.server.demowebsocket.http.repository.GroupMessageRepository
import com.darcy.kotlin.server.demowebsocket.utils.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GroupMessageService @Autowired constructor(
    private val groupMessageRepository: GroupMessageRepository,
    private val userService: UserService,
    private val groupService: GroupService,
    private val groupMemberService: GroupMemberService,
    private val conversationService: ConversationService,
    private val idGenerator: IdGenerator,
) {
    fun sendMessage(
        senderId: Long,
        groupId: Long,
        conversationId: Long,
        content: String
    ): GroupMessage {
        val sender = userService.getUserById(senderId)
        val group = groupService.queryGroupById(groupId)
        validateConversation(conversationId, senderId, groupId)
        validateGroupMember(senderId, groupId)
        val message = GroupMessage(
            // 群消息ID 唯一
            msgId = idGenerator.nextGroupMessageId(),
            sender = sender,
            group = group,
            content = content,
            sendTime = LocalDateTime.now()
        )
        return groupMessageRepository.save(message)
    }

    private fun validateGroupMember(senderId: Long, groupId: Long) {
        val isGroupMember = groupMemberService.isGroupMember(senderId, groupId)
        if (!isGroupMember) {
            throw GroupException.USER_NOT_GROUP_MEMBER
        }
    }

    private fun validateConversation(conversationId: Long, senderId: Long, groupId: Long) {
        val conversation = conversationService.queryOneConversation(conversationId)
        if (conversation.conversationType != Conversation.ConversationType.GROUP) {
            throw ConversationException.CONVERSATION_TYPE_ERROR
        }
        if (conversation.targetId != groupId) {
            throw ConversationException.CONVERSATION_MEMBER_ERROR
        }
    }

    fun queryGroupMessages(userId: Long, groupId: Long, conversationId: Long, page: Int, size: Int): Page<GroupMessage> {
        validateConversation(conversationId, userId, groupId)
        validateGroupMember(userId, groupId)
        val pageable = PageRequest.of(page, size)
        return groupMessageRepository.findGroupMessagePage(groupId, pageable)
    }
}