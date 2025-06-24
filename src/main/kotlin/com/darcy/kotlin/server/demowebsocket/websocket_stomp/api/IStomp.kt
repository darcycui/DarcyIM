package com.darcy.kotlin.server.demowebsocket.websocket_stomp.api

import com.darcy.kotlin.server.demowebsocket.domain.table.MessageEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

interface IStomp {
    @MessageMapping("/chat")
    fun send(sha: SimpMessageHeaderAccessor, @Payload chatMessage: MessageEntity)
}