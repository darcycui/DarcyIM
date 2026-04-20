package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IPrivateMessageApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.exception.ParamsException
import com.darcy.kotlin.server.demowebsocket.http.service.PrivateMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class PrivateMessageController @Autowired constructor(
    val privateMessageService: PrivateMessageService,
) : IPrivateMessageApi {
    override fun sendMessage(params: Map<String, String>): String {
        val senderId = params["senderId"]?.toLongOrNull()
            ?: throw ParamsException.ParamsNotValid(mapOf("senderId" to "发送者ID不能为空"))
        val receiverId = params["receiverId"]?.toLongOrNull()
            ?: throw ParamsException.ParamsNotValid(mapOf("receiverId" to "接收者ID不能为空"))
        val content = params["content"]
            ?: throw ParamsException.ParamsNotValid(mapOf("content" to "消息内容不能为空"))
        val conversationId = params["conversationId"]?.toLongOrNull() ?: throw ParamsException.ParamsNotValid(
            mapOf("conversationId" to "会话ID不能为空")
        )
        val result = privateMessageService.sendMessage(senderId, receiverId, content, conversationId)
        return ResultEntity.success(result).toJsonString()
    }

    override fun queryMessagesByConversation(params: Map<String, String>): String {
        val conversationId = params["conversationId"]?.toLongOrNull() ?: throw ParamsException.ParamsNotValid(
            mapOf("conversationId" to "会话ID不能为空")
        )
        val page = params["page"]?.toIntOrNull() ?: 0
        val size = params["size"]?.toIntOrNull() ?: 2
        val result = privateMessageService.queryBothMessagesPageByConversation(conversationId, page, size)
        return ResultEntity.success(result).toJsonString()
    }
}