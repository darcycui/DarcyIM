package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import com.darcy.kotlin.server.demowebsocket.http.repository.UserRepository
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService @Autowired constructor(
    val userRepository: UserRepository,
    val passwordUtil: PasswordUtil
)
    : UserDetailsService
{
    fun validateUserCredentials(name: String, password: String): UserEntity? {
        val user = userRepository.findByName(name)
        return if (user != null && passwordUtil.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    override
    fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            DarcyLogger.error("用户名不能为空")
            throw Exception("用户名不能为空")
        }
        val user = userRepository.findByName(username)
        if (user == null) {
            DarcyLogger.error("用户不存在")
            throw Exception("用户不存在")
        }
        DarcyLogger.info("用户存在: ${user.username}")
        user.roles = "USER"
        return User.builder().username(user.username)
            .password(user.password)
            .roles(user.roles)
            .disabled(false)
            .build()
    }
}