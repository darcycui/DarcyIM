package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IUserApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController @Autowired constructor(val userService: UserService) : IUserApi {

    override fun createUser(params: Map<String, Any>): String {
        val name = params["name"]?.toString() ?: ""
        val password = params["password"]?.toString() ?: ""
        val userEntity = User(
            username = name,
            passwordHash = password,
            nickname = "",
            avatar = null,
            phone = null,
            email = null,
            gender = null,
            signature = null,
            status = User.UserStatus.NORMAL,
            onlineStatus = User.OnlineStatus.ONLINE,
            lastActiveTime = null,
            deletedAt = null,
            settings = emptyMap(),
            roles = "",
            token = ""
        )
        return ResultEntity.success(userService.createUser(userEntity)).toJsonString()
    }

    override fun getUserById(id: Long): String {
        return ResultEntity.success(userService.getUserById(id)).toJsonString()
    }

    override fun updateUser(params: Map<String, Any>): String {
        val userBean: User = User(
            username = params["name"].toString()
        )
        return kotlin.runCatching {
            if (contains(userBean).not()) {
                return ResultEntity.error(UserException.USER_NOT_EXIST).toJsonString()
            }
            ResultEntity.success(userService.updateUser(userBean)).toJsonString()
        }.onFailure {
            DarcyLogger.error("update user error:", it)
            ResultEntity.error(UserException.USER_UPDATE_ERROR).toJsonString()
        }.onSuccess {
            DarcyLogger.info("update user success")
        }.getOrElse { "update user error" }
    }

    override fun deleteUser(params: Map<String, Any>): String {
        val userBean: User = User(
            username = params["name"].toString()
        )
        return kotlin.runCatching {
            if (contains(userBean).not()) {
                return ResultEntity.error(UserException.USER_NOT_EXIST).toJsonString()
            }
            userService.deleteUser(userBean)
            ResultEntity.success<String>("delete user SUCCESS").toJsonString()
        }.onFailure {
            DarcyLogger.error("delete user error:", it)
            return ResultEntity.error(UserException.USER_DELETE_ERROR).toJsonString()
        }.onSuccess {
            DarcyLogger.info("delete user success")
        }.getOrElse { "default delete error" }
    }

    private fun contains(userBean: User): Boolean {
        val users = userService.getAllUsers()
        return users.isNotEmpty() && users.contains(userBean)
    }

    override fun getAllUsers(): String {
        val users = userService.getAllUsers()
        return ResultEntity.success(users).toJsonString()
    }
}