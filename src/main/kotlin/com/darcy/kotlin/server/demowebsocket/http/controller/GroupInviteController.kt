package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IGroupInviteApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.exception.ParamsException
import com.darcy.kotlin.server.demowebsocket.http.service.GroupInviteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupInviteController @Autowired constructor(
    private val groupInviteService: GroupInviteService
) : IGroupInviteApi {
    override fun createGroupInvite(params: Map<String, String>): String {
        val groupId = params["groupId"]?.toLongOrNull() ?: throw ParamsException.ParamsNotValid(mapOf("groupId" to "群组ID不能为空"))
        val inviterId = params["inviterId"]?.toLongOrNull()
            ?: throw ParamsException.ParamsNotValid(mapOf("inviterId" to "邀请人ID不能为空"))
        val inviteeId = params["inviteeId"]?.toLongOrNull()
            ?: throw ParamsException.ParamsNotValid(mapOf("inviteeId" to "被邀请人ID不能为空"))
        val result = groupInviteService.createGroupInvite(inviterId, inviteeId, groupId)
        return ResultEntity.success(result).toJsonString()
    }

    override fun queryGroupInviteByFromUser(params: Map<String, String>): String {
        val fromUserId = params["fromUserId"]?.toLongOrNull()
            ?: throw ParamsException.ParamsNotValid(mapOf("fromUserId" to "邀请人ID不能为空"))
        val result = groupInviteService.queryGroupInvitesByFromUser(fromUserId)
        return ResultEntity.success(result).toJsonString()
    }

    override fun queryGroupInviteByToUser(params: Map<String, String>): String {
        val toUserId = params["toUserId"]?.toLongOrNull()
            ?: throw ParamsException.ParamsNotValid(mapOf("toUserId" to "被邀请人ID不能为空"))
        val result = groupInviteService.queryGroupInvitesByToUser(toUserId)
        return ResultEntity.success(result).toJsonString()
    }

}