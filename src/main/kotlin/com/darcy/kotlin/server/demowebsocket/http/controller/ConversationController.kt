package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IConversationApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import com.darcy.kotlin.server.demowebsocket.exception.ParamsException
import com.darcy.kotlin.server.demowebsocket.http.service.ConversationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class ConversationController @Autowired constructor(
    val conversationService: ConversationService
) : IConversationApi {
    override fun createConversation(params: Map<String, String>): String {
        // http 参数校验
        val userId = params["userId"]?.toLongOrNull() ?: throw ParamsException.ParamsNotValid(
            mapOf("userId" to "用户ID不能为空")
        )
        val targetId = params["targetId"]?.toLongOrNull() ?: throw ParamsException.ParamsNotValid(
            mapOf("targetId" to "目标用户ID不能为空")
        )
        val conversationType = params["conversationType"]?.toIntOrNull()?.let {
            Conversation.ConversationType.fromCode(it)
        } ?: throw ParamsException.ParamsNotValid(
            mapOf("conversationType" to "会话类型不能为空")
        )
        // 调用 Service 完成业务逻辑
        val result = conversationService.createConversation(userId, conversationType, targetId)
        // 返回 json结果
        return ResultEntity.success(result).toJsonString()
    }

    override fun queryConversations(params: Map<String, String>): String {
        val userId = params["userId"]?.toLongOrNull() ?: 0L
        if (userId == 0L) {
            throw ParamsException.ParamsNotValid(mapOf("userId" to "用户ID不能为空"))
        }
        val result = conversationService.queryConversations(userId)
        return ResultEntity.success(result).toJsonString()
    }

    override fun queryConversationById(params: Map<String, String>): String {
        val conversationId = params["conversationId"]?.toLongOrNull() ?: 0L
        if (conversationId == 0L) {
            throw ParamsException.ParamsNotValid(mapOf("conversationId" to "会话ID不能为空"))
        }
        val result = conversationService.queryOneConversation(conversationId)
        return ResultEntity.success(result).toJsonString()
    }

}