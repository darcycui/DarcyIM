package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IUserApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.UserEntity
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
import com.darcy.kotlin.server.demowebsocket.log.LOGGER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin //跨域注解
class UserController @Autowired constructor(val userService: UserService) : IUserApi {

    override fun createUser(params: Map<String, Any>): String {
        return ResultEntity.success(userService.createUser(params["name"].toString())).toJsonString()
    }

    override fun getUserById(id: Long): String {
        return ResultEntity.success(userService.getUserById(id)).toJsonString()
    }

    override fun updateUser(params: Map<String, Any>): String {
        val userBean: UserEntity = UserEntity(
            id = params["id"].toString().toLong(),
            name = params["name"].toString()
        )
        if (contains(userBean).not()) {
            return ResultEntity.error<String>(-1, "user not exist").toJsonString()
        }
        return ResultEntity.success(userService.updateUser(userBean)).toJsonString()
    }

    override fun deleteUser(params: Map<String, Any>): String {
        val userBean: UserEntity = UserEntity(
            id = params["id"]?.toString()?.toLong() ?: -1L,
            name = params["name"].toString()
        )
        return kotlin.runCatching {
            if (contains(userBean).not()) {
                return ResultEntity.error<String>(-1, "user not exist").toJsonString()
            }
            userService.deleteUser(userBean)
            ResultEntity.success<String>("delete user SUCCESS").toJsonString()
        }.onFailure {
            LOGGER.error("delete user error:", it)
            return ResultEntity.error<String>(-1, "delete user error: $it").toJsonString()
        }.onSuccess {
            LOGGER.info("delete user success")
        }.getOrElse { "default delete error" }
    }

    private fun contains(userBean: UserEntity): Boolean {
        val users = userService.getAllUsers()
        return users.isNotEmpty() && users.contains(userBean)
    }

    override fun getAllUsers(): String {
        val users = userService.getAllUsers()
        return ResultEntity.success(users).toJsonString()
    }
}