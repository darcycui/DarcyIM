package com.darcy.kotlin.server.demowebsocket.websocket_stomp.service

import com.darcy.kotlin.server.demowebsocket.domain.dto.message.GroupMessageDTO
import com.darcy.kotlin.server.demowebsocket.domain.dto.message.PrivateMessageDTO
import com.darcy.kotlin.server.demowebsocket.domain.dto.message.toEntity
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.exception.websocket.STOMPException
import com.darcy.kotlin.server.demowebsocket.http.service.PrivateMessageService
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class STOMPService @Autowired constructor(
    private val websocket: SimpMessagingTemplate,
    private val privateMessageService: PrivateMessageService,
    private val userService: UserService,
) {
    fun sendPrivate(privateMessage: PrivateMessageDTO) {
        kotlin.runCatching {
            val recipient = privateMessage.receiverName
            DarcyLogger.warn("单发消息 -->$recipient")
            // Spring STOMP 单播 Unicast
            websocket.convertAndSendToUser(recipient, "/queue/message", privateMessage)
            val sendUser = userService.getUserById(privateMessage.senderId)
            val receiveUser = userService.getUserById(privateMessage.receiverId)
            privateMessageService.sendMessage(privateMessage.toEntity(sendUser, receiveUser))
        }.onSuccess {
            DarcyLogger.info("send private message SUCCESS")
        }.onFailure {
            DarcyLogger.error("send private message FAILED")
            it.printStackTrace()
            throw STOMPException.STOMP_SEND_PRIVATE_MESSAGE_FAILED
        }
    }

    fun sendAllGroup(groupMessage: GroupMessageDTO) {
        kotlin.runCatching {
            DarcyLogger.warn("群发消息All -->/topic/message")
            // Spring STOMP 广播 Broadcast - 广播给所有订阅者
            websocket.convertAndSend("/topic/message", groupMessage)
        }.onSuccess {
            DarcyLogger.info("send all group message SUCCESS")
        }.onFailure {
            DarcyLogger.error("send all group message FAILED")
            it.printStackTrace()
            throw STOMPException.STOMP_SEND_ALL_GROUP_MESSAGE_FAILED
        }
    }

    fun sendTargetGroup(groupMessage: GroupMessageDTO) {
        kotlin.runCatching {
            val groupId = groupMessage.groupId
            DarcyLogger.warn("群发消息 -->/topic/group/$groupId")
            // Spring STOMP 广播 Broadcast - 只发送给指定群组的订阅者
            websocket.convertAndSend("/topic/group/$groupId", groupMessage)
        }.onSuccess {
            DarcyLogger.info("send target group message SUCCESS")
        }.onFailure {
            DarcyLogger.error("send target group message FAILED")
            it.printStackTrace()
            throw STOMPException.STOMP_SEND_TARGET_GROUP_MESSAGE_FAILED
        }
    }
}