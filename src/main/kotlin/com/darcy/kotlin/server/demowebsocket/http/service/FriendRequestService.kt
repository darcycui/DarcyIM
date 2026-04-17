package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.FriendRequest
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.ParamsException
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.exception.user.UserExceptionSupplier
import com.darcy.kotlin.server.demowebsocket.http.repository.FriendRequestRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class FriendRequestService @Autowired constructor(
    val friendRequestRepository: FriendRequestRepository,
    val userService: UserService
) {

    @Transactional
    fun createFriendRequest(params: Map<String, Any>): FriendRequest {
        val fromUserPhone = params["fromUserPhone"]?.toString() ?: ""
        val toUserPhone = params["toUserPhone"]?.toString() ?: ""
        if (fromUserPhone.isEmpty() or toUserPhone.isEmpty()) {
            throw ParamsException.ParamsNotValid(
                mapOf(
                    "fromUserPhone" to "fromUserPhone 不能为空",
                    "toUserPhone" to "toUserPhone 不能为空"
                )
            )
        }
        val fromUser = userService.getUserByPhone(fromUserPhone)
        val toUser = userService.getUserByPhone(toUserPhone)
        if (fromUser == User.EMPTY || toUser == User.EMPTY) {
            throw UserException.USER_NOT_EXIST
        }
        val friendRequest = FriendRequest(
            fromUser = fromUser,
            toUser = toUser,
            greeting = "你好，我是${fromUser.username}",
            status = FriendRequest.RequestStatus.PENDING,
            remark = "昵称：${toUser.username}",
        ).apply {
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
        return friendRequestRepository.save(friendRequest)
    }

    fun findFriendRequestById(id: Long): FriendRequest? {
        return friendRequestRepository.findById(id).orElseThrow(UserExceptionSupplier(UserException.FRIEND_REQUEST_NOT_EXIST))
    }

    @Transactional
    fun acceptFriendRequest(friendRequestId: Long): Boolean {
        val friendRequest = findFriendRequestById(friendRequestId) ?: return false
        friendRequest.apply {
            status = FriendRequest.RequestStatus.ACCEPTED
            handleTime = LocalDateTime.now()
            handleResult = "已接受"
        }
        friendRequestRepository.save(friendRequest)
        return true
    }

    fun queryByFromUserPhone(fromUserId: Long): List<FriendRequest> {
        return friendRequestRepository.queryByFromUserId(fromUserId)
    }

    fun queryByToUserPhone(toUserPhone: String): List<FriendRequest> {
        return friendRequestRepository.queryByToUserId(toUserPhone)
    }
}