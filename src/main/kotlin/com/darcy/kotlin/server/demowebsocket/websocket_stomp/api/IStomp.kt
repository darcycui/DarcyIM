package com.darcy.kotlin.server.demowebsocket.websocket_stomp.api

import com.darcy.kotlin.server.demowebsocket.domain.dto.message.PrivateMessageDTO
import com.darcy.kotlin.server.demowebsocket.domain.table.message.PrivateMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

interface IStomp {
    @MessageMapping("/sendPrivateMessage")
    fun sendPrivate(sha: SimpMessageHeaderAccessor, @Payload privateMessage: PrivateMessageDTO)

    @MessageMapping("/sendGroupMessage")
    fun sendGroup(sha: SimpMessageHeaderAccessor, @Payload groupMessage: PrivateMessageDTO)
}