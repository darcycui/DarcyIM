package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import com.darcy.kotlin.server.demowebsocket.http.repository.UserDatabaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(val userDatabaseRepository: UserDatabaseRepository) {

    @Transactional
    fun createUser(name: String): UserEntity {
        return userDatabaseRepository.save(UserEntity(name = name))
    }

    fun getUserById(id: Long): UserEntity? {
        return userDatabaseRepository.findById(id).orElse(null)
    }

    @Transactional
    fun updateUser(user: UserEntity): UserEntity? {
        val oldUser = getUserById(user.id)
        if (oldUser != null) {
            return userDatabaseRepository.save(user)
        }
        return null
    }

    fun deleteUser(user: UserEntity) {
        userDatabaseRepository.delete(user)
    }

    fun getAllUsers(): List<UserEntity> {
        return userDatabaseRepository.findAll()
    }
}