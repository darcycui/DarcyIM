package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.ConversationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConversationService @Autowired constructor(
    val conversationRepository: ConversationRepository,
    val userService: UserService,
    private val friendshipService: FriendshipService
) {
    fun createConversation(
        userId: Long,
        conversationType: Conversation.ConversationType,
        targetId: Long
    ): Conversation {
        val user = userService.getUserById(userId)
        if (user.isEmpty()) {
            throw UserException.USER_NOT_EXIST
        }
        validateTarget(userId, conversationType, targetId)
        val existingConversation = conversationRepository.findByUserIdAndConversationTypeAndTargetId(
            userId,
            conversationType,
            targetId
        )
        if (existingConversation != null) {
            return existingConversation
        }
        val conversation = Conversation(
            user = user,
            conversationType = conversationType,
            targetId = targetId
        )
        return conversationRepository.save(conversation)
    }

    private fun validateTarget(userId: Long, conversationType: Conversation.ConversationType, targetId: Long) {
        when (conversationType) {
            Conversation.ConversationType.PRIVATE -> {
                val targetUser = userService.getUserById(targetId)
                if (targetUser.isEmpty()) {
                    throw UserException.USER_NOT_EXIST
                }
                val isFriend = friendshipService.isFriend(userId, targetId)
                if (!isFriend) {
                    throw UserException.FRIENDSHIP_NOT_EXIST
                }
            }

            Conversation.ConversationType.GROUP -> {
                // TODO: 验证群组是否存在
            }
        }
    }

    fun queryConversations(userId: Long): List<Conversation> {
        return conversationRepository.findByUserId(userId)
    }

    fun queryOneConversation(conversationId: Long): Conversation? {
        return conversationRepository.findById(conversationId).orElse(null)
    }
}