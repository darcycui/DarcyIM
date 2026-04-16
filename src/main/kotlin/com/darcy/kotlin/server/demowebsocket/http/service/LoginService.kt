package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.http.repository.UserRepository
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoginService@Autowired constructor(
    val userRepository: UserRepository,
    val passwordUtil: PasswordUtil
)  {

    fun isValidateUser(name: String, password: String): User? {
        val user = userRepository.findByUserName(name)
        return if (user != null && passwordUtil.matches(password, user.passwordHash)) {
            user
        } else {
            null
        }
    }
}