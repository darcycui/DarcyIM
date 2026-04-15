package com.darcy.kotlin.server.demowebsocket.websocket_stomp.controller

import com.darcy.kotlin.server.demowebsocket.domain.table.message.PrivateMessage
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
    override fun send(sha: SimpMessageHeaderAccessor, @Payload chatMessage: PrivateMessage) {
        val sender = sha.user?.name ?: ""
        DarcyLogger.info("sender: $sender message=$chatMessage")
        val recipient = chatMessage.receiver.username
        DarcyLogger.warn("单发消息 -->$recipient")
        websocket.convertAndSendToUser(recipient, "/queue/msg", chatMessage)
    }
}