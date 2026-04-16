package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.UserRepository
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(
    val userRepository: UserRepository,
    val passwordUtil: PasswordUtil
) {

    @Transactional
    fun createUser(userEntity: User): User {
        if (userRepository.existsByUsername(userEntity.username)) {
            throw UserException.USER_NAME_ALREADY_EXIST
        }
        val realUser = userEntity.apply {
            passwordHash = passwordUtil.encode(passwordHash)
        }
        return userRepository.save(realUser)
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    fun updateUser(user: User): User? {
        val oldUser = getUserById(user.id)
        if (oldUser != null) {
            return userRepository.save(user)
        }
        return null
    }

    fun deleteUser(user: User) {
        userRepository.delete(user)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}