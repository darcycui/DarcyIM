package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
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
    fun createUser(userEntity: UserEntity): UserEntity {
        val passwordEncoded = passwordUtil.encode(userEntity.password)
        return userRepository.save(userEntity.copy(password = passwordEncoded))
    }

    fun validateUser(name: String, password: String): UserEntity? {
        val user = userRepository.findByName(name)
        return if (user != null && passwordUtil.matches(password, user.password)) {
            user
        } else {
            null
        }
    }


    fun getUserById(id: Long): UserEntity? {
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    fun updateUser(user: UserEntity): UserEntity? {
        val oldUser = getUserById(user.id)
        if (oldUser != null) {
            return userRepository.save(user)
        }
        return null
    }

    fun deleteUser(user: UserEntity) {
        userRepository.delete(user)
    }

    fun getAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }
}