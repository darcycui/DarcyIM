package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.UserRepository
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterService @Autowired constructor(
    private val userService: UserService
) {

    @Transactional
    fun register(userEntity: User): User {
        return userService.createUser(userEntity)
    }
}