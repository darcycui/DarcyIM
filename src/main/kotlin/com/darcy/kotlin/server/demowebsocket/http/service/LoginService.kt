package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.config.jwt.JwtTokenProvider
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginService @Autowired constructor(
    val jwtTokenProvider: JwtTokenProvider,
    val userService: UserService,
    val passwordUtil: PasswordUtil
) {

    @Transactional
    fun getValidateUser(phone: String, password: String): User {
        val user = userService.queryUserByPhone(phone)
        return if (passwordUtil.matches(password, user.passwordHash)) {
            user.apply {
                val newTokenVersion = this.jwtTokenVersion + 1
                token = jwtTokenProvider.generateToken(user.username, newTokenVersion)
                jwtTokenVersion = newTokenVersion
                userService.updateUser(this)
            }
        } else {
            throw UserException.USER_NAME_PASSWORD_ERROR
        }
    }
}