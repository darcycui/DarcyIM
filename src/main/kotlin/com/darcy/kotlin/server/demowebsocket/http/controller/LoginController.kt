package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.ILoginApi
import com.darcy.kotlin.server.demowebsocket.config.jwt.JwtTokenProvider
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.CustomUserDetailsService
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
//import com.darcy.kotlin.server.demowebsocket.http.service.CustomUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController @Autowired constructor(
    val userService: UserService,
    val jwtTokenProvider: JwtTokenProvider
) : ILoginApi {
    override fun login(params: Map<String, Any>): String {
        // username: String, password: String
        val username = params["name"] as? String ?: ""
        val password = params["password"] as? String ?: ""
        return if (username.isEmpty() || password.isEmpty()) {
            ResultEntity.error(UserException.USER_NAME_PASSWORD_EMPTY).toJsonString()
        } else {
            val existUser = userService.validateUser(username, password)
            if (existUser == null) {
                ResultEntity.error(UserException.USER_NAME_PASSWORD_ERROR).toJsonString()
            } else {
                existUser.token = jwtTokenProvider.generateToken(username)
                ResultEntity.success<UserEntity>(existUser).toJsonString()
            }
        }
    }
}