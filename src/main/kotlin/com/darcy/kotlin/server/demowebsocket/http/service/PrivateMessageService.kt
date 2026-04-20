package  com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.domain.table.message.PrivateMessage
import com.darcy.kotlin.server.demowebsocket.exception.ConversationException
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.PrivateMessageRepository
import com.darcy.kotlin.server.demowebsocket.utils.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PrivateMessageService @Autowired constructor(
    private val privateMessageRepository: PrivateMessageRepository,
    private val conversationService: ConversationService,
    private val friendshipService: FriendshipService,
    private val userService: UserService,
    private val idGenerator: IdGenerator
) {
    fun sendMessage(
        senderId: Long,
        receiverId: Long,
        content: String,
        conversationId: Long
    ): PrivateMessage {
        val sender = userService.getUserById(senderId)
        val receiver = userService.getUserById(receiverId)
        validateUser(sender, receiver)
        validateFriendship(senderId, receiverId)
        validateConversation(conversationId, senderId, receiverId)
        val msgId = idGenerator.nextMessageId()
        val message = PrivateMessage(
            msgId = msgId,
            sender = sender,
            receiver = receiver,
            content = content,
            sendTime = LocalDateTime.now()
        )
        return privateMessageRepository.save(message)
    }

    private fun validateConversation(conversationId: Long, senderId: Long = -1, receiverId: Long = -1) {
        val conversation = conversationService.queryOneConversation(conversationId)
        if (conversation.conversationType != Conversation.ConversationType.PRIVATE) {
            throw ConversationException.CONVERSATION_TYPE_ERROR
        }
        val isSenderValidate = conversation.user.id == senderId && conversation.targetId == receiverId
        val isReceiverValidate = conversation.targetId == senderId && conversation.user.id == receiverId
        if (senderId > 0 && receiverId > 0 && isSenderValidate.not() && isReceiverValidate.not()) {
            throw ConversationException.CONVERSATION_NOT_EXIST
        }
    }

    private fun validateFriendship(senderId: Long, receiverId: Long) {
        if (!friendshipService.isFriend(senderId, receiverId)) {
            throw UserException.FRIENDSHIP_NOT_EXIST
        }
    }

    private fun validateUser(sender: User, receiver: User) {
        if (sender.isEmpty() || receiver.isEmpty()) {
            throw UserException.USER_NOT_EXIST
        }
    }

    fun queryBothMessagesAllByConversation(conversationId: Long): List<PrivateMessage> {
        validateConversation(conversationId)
        val conversation = conversationService.queryOneConversation(conversationId)
        val senderId = conversation.user.id
        val receiverId = conversation.targetId
        return privateMessageRepository.findBothMessagesAll(senderId, receiverId)
    }

    fun queryBothMessagesPageByConversation(conversationId: Long, page: Int, size: Int): Page<PrivateMessage> {
        validateConversation(conversationId)
        val conversation = conversationService.queryOneConversation(conversationId)
        val senderId = conversation.user.id
        val receiverId = conversation.targetId
        val pageable = PageRequest.of(page, size)
        return privateMessageRepository.findBothMessagesPage(senderId, receiverId, pageable)

        // 获取分页数据 result
        // result.content  // 当前页的消息列表
        // result.totalPages  // 总页数
        // result.totalElements  // 总记录数
        // result.number  // 当前页码（从 0 开始）
    }
}