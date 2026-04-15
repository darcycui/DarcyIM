//package com.darcy.kotlin.server.demowebsocket.http.service
//
//import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
//import com.darcy.kotlin.server.demowebsocket.domain.table.PrivateMessage
//import com.darcy.kotlin.server.demowebsocket.domain.table.group.GroupMember
//import com.darcy.kotlin.server.demowebsocket.domain.table.group.GroupMessage
//import com.darcy.kotlin.server.demowebsocket.domain.table.read.MessageReadStatus
//import com.darcy.kotlin.server.demowebsocket.http.repository.ConversationRepository
//import com.darcy.kotlin.server.demowebsocket.http.repository.PrivateMessageRepository
//import com.darcy.kotlin.server.demowebsocket.http.repository.UserRepository
//import com.darcy.kotlin.server.demowebsocket.utils.IdGenerator
//import org.slf4j.LoggerFactory
//import org.springframework.cache.annotation.Cacheable
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.domain.Sort
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//import org.springframework.transaction.support.TransactionTemplate
//import java.time.LocalDateTime
//import java.util.*
//import kotlin.math.min
//
//@Service
//@Transactional(readOnly = true)
//class MessageService(
//    private val privateMessageRepository: PrivateMessageRepository,
////    private val groupMessageRepository: GroupMessageRepository,
//    private val conversationRepository: ConversationRepository,
////    private val messageReadStatusRepository: MessageReadStatusRepository,
//    private val userRepository: UserRepository,
////    private val friendRelationshipRepository: FriendRelationshipRepository,
////    private val groupMemberRepository: GroupMemberRepository,
////    private val groupRepository: GroupRepository,
//    private val idGenerator: IdGenerator,
//    private val transactionTemplate: TransactionTemplate
//) {
//
//    companion object {
//        private val log = LoggerFactory.getLogger(MessageService::class.java)
//        private const val MAX_MESSAGE_LENGTH = 5000
//        private const val RECALL_WINDOW_MINUTES = 2L
//    }
//
//    /**
//     * 发送私聊消息
//     */
//    @Transactional
//    fun sendPrivateMessage(
//        senderId: Long,
//        receiverId: Long,
//        msgType: PrivateMessage.MessageType,
//        content: String?,
//        extraData: Map<String, Any>? = null
//    ): PrivateMessage {
//        // 验证消息内容
//        validateMessageContent(content, msgType)
//
//        // 获取用户
//        val sender = userRepository.findById(senderId)
//            .orElseThrow { BusinessException("发送者不存在") }
//        val receiver = userRepository.findById(receiverId)
//            .orElseThrow { BusinessException("接收者不存在") }
//
//        // 验证好友关系
//        val relationship = friendRelationshipRepository.findByUserAndFriend(sender, receiver)
//            .orElseThrow { BusinessException("非好友关系，无法发送消息") }
//
//        if (relationship.isBlocked) {
//            throw BusinessException("你已被对方拉黑")
//        }
//
//        // 生成消息
//        val message = PrivateMessage(
//            msgId = idGenerator.nextMessageId(),
//            sender = sender,
//            receiver = receiver,
//            msgType = msgType,
//            content = content,
//            extraData = extraData ?: emptyMap(),
//            seqId = System.currentTimeMillis(),
//            sendTime = LocalDateTime.now()
//        )
//
//        // 保存消息
//        val savedMessage = privateMessageRepository.save(message)
//
//        // 更新会话
//        updateConversationForPrivateMessage(savedMessage)
//
//        // 异步推送
//        asyncPushPrivateMessage(savedMessage)
//
//        log.info("消息发送成功: sender={}, receiver={}, msgId={}", senderId, receiverId, savedMessage.msgId)
//
//        return savedMessage
//    }
//
//    /**
//     * 发送群消息
//     */
//    @Transactional
//    fun sendGroupMessage(
//        senderId: Long,
//        groupId: Long,
//        msgType: GroupMessage.MessageType,
//        content: String?,
//        extraData: Map<String, Any>? = null,
//        atAll: Boolean = false,
//        atUsers: List<Long>? = null
//    ): GroupMessage {
//        validateMessageContent(content, PrivateMessage.MessageType.TEXT) // 使用相同验证逻辑
//
//        val sender = userRepository.findById(senderId)
//            .orElseThrow { BusinessException("发送者不存在") }
//        val group = groupRepository.findById(groupId)
//            .orElseThrow { BusinessException("群组不存在") }
//
//        // 验证群成员权限
//        val member = groupMemberRepository.findByGroupIdAndUserId(groupId, senderId)
//            .orElseThrow { BusinessException("你不是群成员") }
//
//        if (member.isMuted()) {
//            throw BusinessException("你已被禁言")
//        }
//
//        // 生成群消息
//        val message = GroupMessage(
//            msgId = idGenerator.nextMessageId(),
//            group = group,
//            sender = sender,
//            msgType = msgType,
//            content = content,
//            extraData = extraData ?: emptyMap(),
//            seqId = System.currentTimeMillis(),
//            sendTime = LocalDateTime.now(),
//            isAtAll = atAll,
//            atUsers = atUsers ?: emptyList(),
//            totalMembers = group.currentMembers
//        )
//
//        val savedMessage = groupMessageRepository.save(message)
//
//        // 更新所有群成员的会话
//        updateConversationsForGroupMessage(savedMessage, group, senderId)
//
//        // 异步推送群消息
//        asyncPushGroupMessage(savedMessage, group.id)
//
//        log.info("群消息发送成功: sender={}, group={}, msgId={}", senderId, groupId, savedMessage.msgId)
//
//        return savedMessage
//    }
//
//    /**
//     * 获取历史消息
//     */
//    fun getPrivateHistoryMessages(
//        userId1: Long,
//        userId2: Long,
//        beforeTime: LocalDateTime? = null,
//        page: Int = 0,
//        size: Int = 20
//    ): List<PrivateMessage> {
//        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sendTime"))
//        val time = beforeTime ?: LocalDateTime.now()
//
//        return privateMessageRepository.findHistoryMessages(userId1, userId2, time, pageable).content
//    }
//
//    /**
//     * 获取未读消息
//     */
//    fun getUnreadPrivateMessages(userId: Long, friendId: Long): List<PrivateMessage> {
//        return privateMessageRepository.findBySenderIdAndReceiverIdAndIsReadFalse(friendId, userId)
//    }
//
//    /**
//     * 标记消息为已读
//     */
//    @Transactional
//    fun markMessagesAsRead(
//        userId: Long,
//        msgIds: List<String>,
//        conversationType: Conversation.ConversationType,
//        targetId: Long
//    ) {
//        if (msgIds.isEmpty()) return
//
//        val now = LocalDateTime.now()
//
//        when (conversationType) {
//            Conversation.ConversationType.PRIVATE -> {
//                // 私聊消息
//                privateMessageRepository.markMessagesAsRead(userId, msgIds, now)
//
//                // 更新会话未读数
//                conversationRepository.findByUserIdAndConversationTypeAndTargetId(userId, conversationType, targetId)
//                    ?.let { conversation ->
//                        transactionTemplate.execute {
//                            conversation.unreadCount = maxOf(0, conversation.unreadCount - msgIds.size)
//                            conversationRepository.save(conversation)
//                        }
//                    }
//            }
//            Conversation.ConversationType.GROUP -> {
//                // 群消息
//                msgIds.forEach { msgId ->
//                    val status = MessageReadStatus(
//                        msgId = msgId,
//                        user = userRepository.getReferenceById(userId),
//                        conversationType = conversationType,
//                        targetId = targetId
//                    )
//                    status.markAsRead(now)
//                    messageReadStatusRepository.save(status)
//
//                    // 更新群消息的已读计数
//                    groupMessageRepository.findByMsgId(msgId)?.let { msg ->
//                        msg.incrementReadCount()
//                        groupMessageRepository.save(msg)
//                    }
//                }
//
//                // 更新群成员的未读数
//                groupMemberRepository.findByGroupIdAndUserId(targetId, userId)?.let { member ->
//                    member.clearUnread()
//                    groupMemberRepository.save(member)
//                }
//            }
//        }
//
//        log.debug("消息标记为已读: userId={}, msgCount={}, type={}", userId, msgIds.size, conversationType)
//    }
//
//    /**
//     * 撤回消息
//     */
//    @Transactional
//    fun recallMessage(
//        userId: Long,
//        msgId: String,
//        conversationType: Conversation.ConversationType
//    ) {
//        val now = LocalDateTime.now()
//
//        when (conversationType) {
//            Conversation.ConversationType.PRIVATE -> {
//                val message = privateMessageRepository.findByMsgId(msgId)
//                    .orElseThrow { BusinessException("消息不存在") }
//
//                if (message.sender.id != userId) {
//                    throw BusinessException("只有发送者可以撤回消息")
//                }
//
//                if (!message.canRecall(RECALL_WINDOW_MINUTES)) {
//                    throw BusinessException("消息已超过撤回时间限制")
//                }
//
//                message.recall(now)
//                privateMessageRepository.save(message)
//            }
//            Conversation.ConversationType.GROUP -> {
//                val message = groupMessageRepository.findByMsgId(msgId)
//                    .orElseThrow { BusinessException("消息不存在") }
//
//                // 检查撤回权限：发送者或管理员
//                val isSender = message.sender.id == userId
//                val isAdmin = groupMemberRepository.findByGroupIdAndUserId(message.group.id, userId)
//                    ?.let { it.role == GroupMember.MemberRole.OWNER || it.role == GroupMember.MemberRole.ADMIN }
//                    ?: false
//
//                if (!isSender && !isAdmin) {
//                    throw BusinessException("没有权限撤回此消息")
//                }
//
//                if (now.isAfter(message.sendTime.plusMinutes(RECALL_WINDOW_MINUTES))) {
//                    throw BusinessException("消息已超过撤回时间限制")
//                }
//
//                message.recall(now)
//                groupMessageRepository.save(message)
//            }
//        }
//
//        // 发送撤回通知
//        sendRecallNotification(msgId, conversationType)
//
//        log.info("消息已撤回: userId={}, msgId={}, type={}", userId, msgId, conversationType)
//    }
//
//    /**
//     * 删除消息
//     */
//    @Transactional
//    fun deleteMessage(userId: Long, msgId: String, conversationType: Conversation.ConversationType) {
//        when (conversationType) {
//            Conversation.ConversationType.PRIVATE -> {
//                val message = privateMessageRepository.findByMsgId(msgId)
//                    .orElseThrow { BusinessException("消息不存在") }
//
//                when (userId) {
//                    message.sender.id -> message.isDeletedBySender = true
//                    message.receiver.id -> message.isDeletedByReceiver = true
//                    else -> throw BusinessException("无权删除此消息")
//                }
//
//                privateMessageRepository.save(message)
//            }
//            Conversation.ConversationType.GROUP -> {
//                // 群消息通常是软删除逻辑
//                // 这里可以根据业务需求实现
//            }
//        }
//
//        log.debug("消息已删除: userId={}, msgId={}", userId, msgId)
//    }
//
//    /**
//     * 获取未读统计
//     */
//    @Cacheable(value = ["unreadStats"], key = "#userId")
//    fun getUnreadStatistics(userId: Long): UnreadStatistics {
//        // 私聊未读
//        val privateUnread = privateMessageRepository.countUnreadMessages(userId)
//
//        // 群聊未读
//        val groupMembers = groupMemberRepository.findByUserId(userId)
//        val groupUnread = groupMembers.sumOf { it.unreadCount.toLong() }
//
//        // 会话列表
//        val conversations = conversationRepository.findByUserIdOrderByLastMsgTimeDesc(userId)
//
//        return UnreadStatistics(
//            privateUnread = privateUnread,
//            groupUnread = groupUnread,
//            totalUnread = privateUnread + groupUnread,
//            conversations = conversations.map { conv ->
//                ConversationSummary(
//                    id = conv.id,
//                    conversationType = conv.conversationType,
//                    targetId = conv.targetId,
//                    lastMsgContent = conv.lastMsgContent,
//                    lastMsgTime = conv.lastMsgTime,
//                    unreadCount = conv.unreadCount,
//                    isMuted = conv.isMuted,
//                    isPinned = conv.isPinned
//                )
//            }
//        )
//    }
//
//    /**
//     * 更新私聊消息的会话
//     */
//    private fun updateConversationForPrivateMessage(message: PrivateMessage) {
//        val senderId = message.sender.id
//        val receiverId = message.receiver.id
//
//        // 更新发送者的会话
//        updateOrCreateConversation(
//            userId = senderId,
//            conversationType = Conversation.ConversationType.PRIVATE,
//            targetId = receiverId,
//            msgId = message.msgId,
//            content = message.content ?: "[非文本消息]",
//            msgType = message.msgType.ordinal,
//            senderId = senderId,
//            sendTime = message.sendTime,
//            increaseUnread = false
//        )
//
//        // 更新接收者的会话
//        updateOrCreateConversation(
//            userId = receiverId,
//            conversationType = Conversation.ConversationType.PRIVATE,
//            targetId = senderId,
//            msgId = message.msgId,
//            content = message.content ?: "[非文本消息]",
//            msgType = message.msgType.ordinal,
//            senderId = senderId,
//            sendTime = message.sendTime,
//            increaseUnread = true
//        )
//    }
//
//    /**
//     * 更新群消息的会话
//     */
//    private fun updateConversationsForGroupMessage(
//        message: GroupMessage,
//        group: Group,
//        senderId: Long
//    ) {
//        val members = groupMemberRepository.findByGroupId(group.id)
//
//        members.forEach { member ->
//            val increaseUnread = member.user.id != senderId && !member.isMuted()
//
//            updateOrCreateConversation(
//                userId = member.user.id,
//                conversationType = Conversation.ConversationType.GROUP,
//                targetId = group.id,
//                msgId = message.msgId,
//                content = message.content ?: "[非文本消息]",
//                msgType = message.msgType.ordinal,
//                senderId = senderId,
//                sendTime = message.sendTime,
//                increaseUnread = increaseUnread
//            )
//
//            if (increaseUnread) {
//                member.incrementUnread()
//                groupMemberRepository.save(member)
//            }
//        }
//    }
//
//    /**
//     * 更新或创建会话
//     */
//    private fun updateOrCreateConversation(
//        userId: Long,
//        conversationType: Conversation.ConversationType,
//        targetId: Long,
//        msgId: String,
//        content: String,
//        msgType: Int,
//        senderId: Long,
//        sendTime: LocalDateTime,
//        increaseUnread: Boolean
//    ) {
//        val conversation = conversationRepository
//            .findByUserIdAndConversationTypeAndTargetId(userId, conversationType, targetId)
//            .orElseGet {
//                Conversation(
//                    user = userRepository.getReferenceById(userId),
//                    conversationType = conversationType,
//                    targetId = targetId
//                )
//            }
//
//        conversation.updateWithMessage(msgId, content, msgType, senderId, sendTime, increaseUnread)
//        conversationRepository.save(conversation)
//    }
//
//    /**
//     * 验证消息内容
//     */
//    private fun validateMessageContent(content: String?, msgType: PrivateMessage.MessageType) {
//        when (msgType) {
//            PrivateMessage.MessageType.TEXT -> {
//                if (content.isNullOrBlank()) {
//                    throw BusinessException("消息内容不能为空")
//                }
//                if (content.length > MAX_MESSAGE_LENGTH) {
//                    throw BusinessException("消息内容过长")
//                }
//            }
//            PrivateMessage.MessageType.IMAGE,
//            PrivateMessage.MessageType.VOICE,
//            PrivateMessage.MessageType.VIDEO,
//            PrivateMessage.MessageType.FILE -> {
//                if (content.isNullOrBlank()) {
//                    throw BusinessException("文件信息不能为空")
//                }
//            }
//            else -> {
//                // 其他类型可根据需要验证
//            }
//        }
//    }
//
//    private fun asyncPushPrivateMessage(message: PrivateMessage) {
//        // 实现消息推送逻辑
//        // 可以使用消息队列如 RabbitMQ/Kafka
//    }
//
//    private fun asyncPushGroupMessage(message: GroupMessage, groupId: Long) {
//        // 实现群消息推送逻辑
//    }
//
//    private fun sendRecallNotification(msgId: String, conversationType: Conversation.ConversationType) {
//        // 发送撤回通知
//    }
//
//    data class UnreadStatistics(
//        val privateUnread: Long,
//        val groupUnread: Long,
//        val totalUnread: Long,
//        val conversations: List<ConversationSummary>
//    )
//
//    data class ConversationSummary(
//        val id: Long,
//        val conversationType: Conversation.ConversationType,
//        val targetId: Long,
//        val lastMsgContent: String,
//        val lastMsgTime: LocalDateTime?,
//        val unreadCount: Int,
//        val isMuted: Boolean,
//        val isPinned: Boolean
//    )
//}
//
//class BusinessException(message: String) : RuntimeException(message)