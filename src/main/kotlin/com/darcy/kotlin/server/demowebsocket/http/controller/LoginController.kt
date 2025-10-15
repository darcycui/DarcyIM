package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.ILoginApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import com.darcy.kotlin.server.demowebsocket.http.service.CustomUserDetailService
//import com.darcy.kotlin.server.demowebsocket.http.service.CustomUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController @Autowired constructor(
    val customUserDetailService: CustomUserDetailService
) : ILoginApi {
    override fun login(params: Map<String, Any>): String {
        // username: String, password: String
        val username = params["name"] as? String ?: ""
        val password = params["password"] as? String ?: ""
        return if (username.isEmpty() || password.isEmpty()) {
            ResultEntity.error<String>(-1, "用户名或密码不能为空").toJsonString()
        } else {
            val existUser = customUserDetailService.validateUserCredentials(username, password)
            if (existUser == null) {
                ResultEntity.error<String>(-2, "用户名或密码错误").toJsonString()
            } else {
                ResultEntity.success<UserEntity>(existUser).toJsonString()
            }
        }
    }
}