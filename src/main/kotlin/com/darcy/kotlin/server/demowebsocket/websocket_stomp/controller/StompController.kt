package com.darcy.kotlin.server.demowebsocket.websocket_stomp.controller

import com.darcy.kotlin.server.demowebsocket.domain.dto.message.GroupMessageDTO
import com.darcy.kotlin.server.demowebsocket.domain.dto.message.PrivateMessageDTO
import com.darcy.kotlin.server.demowebsocket.domain.dto.message.toEntity
import com.darcy.kotlin.server.demowebsocket.http.service.PrivateMessageService
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import com.darcy.kotlin.server.demowebsocket.websocket_stomp.api.IStomp
import com.darcy.kotlin.server.demowebsocket.websocket_stomp.service.STOMPService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class StompController @Autowired constructor(
    private val stompService: STOMPService
) : IStomp {
    override fun sendPrivate(sha: SimpMessageHeaderAccessor, @Payload privateMessage: PrivateMessageDTO) {
        DarcyLogger.info("private message=$privateMessage")
        val sender = sha.user?.name ?: ""
        DarcyLogger.info("private sender: $sender message=$privateMessage")
        stompService.sendPrivate(privateMessage)
    }

    override fun sendAllGroup(sha: SimpMessageHeaderAccessor, @Payload groupMessage: GroupMessageDTO) {
        DarcyLogger.info("all group message=$groupMessage")
        val sender = sha.user?.name ?: ""
        DarcyLogger.info("all group sender: $sender message=$groupMessage")
        stompService.sendAllGroup(groupMessage)
    }

    override fun sendTargetGroup(sha: SimpMessageHeaderAccessor, @Payload groupMessage: GroupMessageDTO) {
        DarcyLogger.info("target group message=$groupMessage")
        val sender = sha.user?.name ?: ""
        DarcyLogger.info("target group sender: $sender message=$groupMessage")
        stompService.sendTargetGroup(groupMessage)
    }
}