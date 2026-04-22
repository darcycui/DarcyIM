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
        if (userEntity.username.isBlank() or userEntity.username.isEmpty()) {
            throw UserException.USER_NAME_EMPTY
        }
        if (userRepository.existsByUsername(userEntity.username)) {
            throw UserException.USER_NAME_ALREADY_EXIST
        }
        if (userRepository.existsByPhone(userEntity.phone)) {
            throw UserException.USER_PHONE_ALREADY_EXIST
        }
        if (userRepository.existsByEmail(userEntity.email)) {
            throw UserException.USER_EMAIL_ALREADY_EXIST
        }
        val realUser = userEntity.apply {
            passwordHash = passwordUtil.encode(passwordHash)
        }
        return userRepository.save(realUser)
    }

    fun getUserById(id: Long): User {
        val user = userRepository.findById(id)
        if (user.isEmpty){
            throw UserException.USER_NOT_EXIST
        }
        return user.get()
    }

    fun getUserByPhone(phone: String): User? {
        return userRepository.findByPhone(phone)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional
    fun updateUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(user: User) {
        userRepository.delete(user)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}