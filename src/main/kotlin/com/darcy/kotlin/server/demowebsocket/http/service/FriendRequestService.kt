package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.FriendRequest
import com.darcy.kotlin.server.demowebsocket.domain.table.Friendship
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.FriendRequestRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FriendRequestService @Autowired constructor(
    val friendRequestRepository: FriendRequestRepository,
    val userService: UserService,
    val friendshipService: FriendshipService
) {

    @Transactional
    fun createFriendRequest(fromUserId: Long, toUserId: Long, params: Map<String, Any>): FriendRequest {
        val fromUser = userService.getUserById(fromUserId)
        val toUser = userService.getUserById(toUserId)
        if (fromUser.isEmpty() or toUser.isEmpty()) {
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

    fun findFriendRequestById(id: Long): FriendRequest {
        val friendRequest = friendRequestRepository.findById(id)
        if (friendRequest.isEmpty) {
            throw UserException.FRIEND_REQUEST_NOT_EXIST
        }
        return friendRequest.get()
    }

    @Transactional
    fun acceptFriendRequest(friendRequestId: Long): List<Friendship> {
        val friendRequest = findFriendRequestById(friendRequestId)
        friendRequest.apply {
            status = FriendRequest.RequestStatus.ACCEPTED
            handleTime = LocalDateTime.now()
            handleResult = "已接受"
        }
        friendRequestRepository.save(friendRequest)
        return friendshipService.createFriendship(friendRequest.fromUser.id, friendRequest.toUser.id)
    }

    @Transactional
    fun rejectFriendRequest(friendRequestId: Long): FriendRequest {
        val friendRequest = findFriendRequestById(friendRequestId)
        friendRequest.apply {
            status = FriendRequest.RequestStatus.REJECTED
            handleTime = LocalDateTime.now()
            handleResult = "已拒绝"
        }
        return friendRequestRepository.save(friendRequest)
    }

    @Transactional
    fun ignoreFriendRequest(friendRequestId: Long): FriendRequest {
        val friendRequest = findFriendRequestById(friendRequestId)
        friendRequest.apply {
            status = FriendRequest.RequestStatus.IGNORED
            handleTime = LocalDateTime.now()
            handleResult = "已忽略"
        }
        return friendRequestRepository.save(friendRequest)
    }

    fun queryByFromUserPhone(fromUserId: Long): List<FriendRequest> {
        return friendRequestRepository.findByFromUserId(fromUserId)
    }

    fun queryByToUserId(toUserId: Long): List<FriendRequest> {
        return friendRequestRepository.findByToUserId(toUserId)
    }
}