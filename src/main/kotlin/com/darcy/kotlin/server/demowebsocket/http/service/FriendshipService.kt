package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Friendship
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.FriendshipRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendshipService @Autowired constructor(
    val friendshipRepository: FriendshipRepository,
    val userService: UserService
) {
    @Transactional
    fun createFriendship(userId: Long, friendId: Long): List<Friendship> {
        val user = userService.getUserById(userId) ?: throw UserException.USER_NOT_EXIST
        val friend = userService.getUserById(friendId) ?: throw UserException.USER_NOT_EXIST
        val result1 = friendshipRepository.save(Friendship(user, friend))
        val result2 = friendshipRepository.save(Friendship(friend, user))
        return listOf(result1, result2)
    }

    @Transactional
    fun queryFriendships(userId: Long): List<User> {
        val friends = friendshipRepository.findByUserId(userId)
        return friends.map {
            it.friend
        }
    }

    @Transactional
    fun isFriend(userId: Long, friendId: Long): Boolean {
        val result = friendshipRepository.findByUserIdAndFriendId(userId, friendId)
        return result != null
    }
}