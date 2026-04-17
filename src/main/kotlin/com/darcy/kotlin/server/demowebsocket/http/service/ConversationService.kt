package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.exception.ConversationException
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.ConversationRepository
import com.darcy.kotlin.server.demowebsocket.utils.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConversationService @Autowired constructor(
    private val conversationRepository: ConversationRepository,
    private val userService: UserService,
    private val friendshipService: FriendshipService,
    private val idGenerator: IdGenerator
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
        val conversationId = idGenerator.nextConversationId()
        val conversation = Conversation(
            conversationId = conversationId,
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

    fun queryOneConversation(conversationId: Long): Conversation {
        val conversation = conversationRepository.findById(conversationId)
        if (conversation.isEmpty) {
            throw ConversationException.CONVERSATION_NOT_EXIST
        }
        return conversation.get()
    }
}