package com.darcy.kotlin.server.demowebsocket.websocket_stomp.controller

import com.darcy.kotlin.server.demowebsocket.domain.dto.message.GroupMessageDTO
import com.darcy.kotlin.server.demowebsocket.domain.dto.message.PrivateMessageDTO
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import com.darcy.kotlin.server.demowebsocket.websocket_stomp.api.IStomp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class StompController @Autowired constructor(
    val websocket: SimpMessagingTemplate
) : IStomp {
    override fun sendPrivate(sha: SimpMessageHeaderAccessor, @Payload privateMessage: PrivateMessageDTO) {
        val sender = sha.user?.name ?: ""
        DarcyLogger.info("private sender: $sender message=$privateMessage")
        val recipient = privateMessage.receiverName
        DarcyLogger.warn("单发消息 -->$recipient")
        // Spring STOMP 单播 Unicast
        websocket.convertAndSendToUser(recipient, "/queue/message", privateMessage)
    }

    override fun sendAllGroup(sha: SimpMessageHeaderAccessor, groupMessage: GroupMessageDTO) {
        val sender = sha.user?.name ?: ""
        DarcyLogger.info("all group sender: $sender message=$groupMessage")
        DarcyLogger.warn("群发消息All -->/topic/message")
        // Spring STOMP 广播 Broadcast
        websocket.convertAndSend("/topic/message", groupMessage)
    }

    override fun sendTargetGroup(sha: SimpMessageHeaderAccessor, groupMessage: GroupMessageDTO) {
        val sender = sha.user?.name ?: ""
        val groupId = groupMessage.groupId
        DarcyLogger.info("target group sender: $sender message=$groupMessage")
        DarcyLogger.warn("群发消息 -->/topic/group/$groupId")
        // Spring STOMP 广播 Broadcast - 只发送给指定群组的订阅者
        websocket.convertAndSend("/topic/group/$groupId", groupMessage)
    }
}