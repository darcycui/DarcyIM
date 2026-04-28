package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.UserRepository
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoginService@Autowired constructor(
    val userRepository: UserRepository,
    val passwordUtil: PasswordUtil
)  {

    fun isValidateUser(phone: String, password: String): User {
        val user = userRepository.findByPhone(phone) ?: throw UserException.USER_NOT_EXIST
        return if (passwordUtil.matches(password, user.passwordHash)) {
            user
        } else {
            throw UserException.USER_NAME_PASSWORD_ERROR
        }
    }
}