package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IFriendRequestApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.FriendRequestService
import com.darcy.kotlin.server.demowebsocket.http.service.FriendshipService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class FriendRequestController @Autowired constructor(
    val friendRequestService: FriendRequestService,
    val friendshipService: FriendshipService
) : IFriendRequestApi {
    override fun createFriendRequest(params: Map<String, Any>): String {
        val result = friendRequestService.createFriendRequest(params)
        return ResultEntity.success(result).toJsonString()
    }

    override fun acceptFriendRequest(params: Map<String, Any>): String {
        val friendRequestId = (params["friendRequestId"] as? String)?.toLongOrNull() ?: 0L
        val friendRequest = friendRequestService.findFriendRequestById(friendRequestId)
        friendRequest?.let {
            val isAccepted = friendRequestService.acceptFriendRequest(friendRequestId)
            if (!isAccepted) {
                throw UserException.FRIEND_REQUEST_ACCEPT_FAILED
            }
            val result = friendshipService.createFriendship(it.fromUser.id, it.toUser.id)
            return ResultEntity.success(result).toJsonString()
        }?: run {
            throw UserException.FRIEND_REQUEST_NOT_EXIST
        }
    }

    override fun rejectFriendRequest(params: Map<String, Any>): String {
        TODO("Not yet implemented")
    }

    override fun ignoreFriendRequest(params: Map<String, Any>): String {
        TODO("Not yet implemented")
    }

    override fun queryFriendRequestByFromUser(params: Map<String, Any>): String {
        val fromUserId = params["fromUserId"] as? Long ?: 0L
        val result = friendRequestService.queryByFromUserPhone(fromUserId)
        return ResultEntity.success(result).toJsonString()
    }

    override fun queryFriendRequestByToUser(params: Map<String, Any>): String {
        val toUserPhone = params["toUserPhone"] as? String ?: ""
        val result = friendRequestService.queryByToUserPhone(toUserPhone)
        return ResultEntity.success(result).toJsonString()
    }
}