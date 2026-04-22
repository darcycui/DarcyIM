package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.ILoginApi
import com.darcy.kotlin.server.demowebsocket.config.jwt.JwtTokenProvider
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.dto.toDTO
import com.darcy.kotlin.server.demowebsocket.exception.ParamsException
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController @Autowired constructor(
    val loginService: LoginService,
    val jwtTokenProvider: JwtTokenProvider
) : ILoginApi {
    override fun login(params: Map<String, String>): String {
        // username: String, password: String
        val username = params["name"] ?: throw ParamsException.ParamsNotValid(mapOf("name" to "用户名不能为空"))
        val password = params["password"] ?: throw ParamsException.ParamsNotValid(mapOf("password" to "密码不能为空"))

        val existUser = loginService.isValidateUser(username, password)
        if (existUser == null) {
            return ResultEntity.error(UserException.USER_NAME_PASSWORD_ERROR).toJsonString()
        } else {
            existUser.token = jwtTokenProvider.generateToken(username)
            return ResultEntity.success(existUser.toDTO()).toJsonString()
        }
    }
}