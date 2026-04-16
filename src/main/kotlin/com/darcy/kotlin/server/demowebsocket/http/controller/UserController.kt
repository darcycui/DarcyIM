package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IUserApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class UserController @Autowired constructor(val userService: UserService) : IUserApi {

    override fun createUser(params: Map<String, Any>): String {
        val settings = if (params.containsKey("settings")) {
            @Suppress("UNCHECKED_CAST")
            params["settings"] as? Map<String, Any> ?: emptyMap()
        } else {
            emptyMap()
        }

        val createdAt = TimeUtil.parseDataTime(params["createdAt"])
        DarcyLogger.info("-->createdAt: $createdAt")
        val updatedAt = TimeUtil.parseDataTime(params["updatedAt"])
        DarcyLogger.info("-->updatedAt: $updatedAt")

        val userEntity = User(
            username = params["username"]?.toString() ?: "",
            passwordHash = params["password"]?.toString() ?: "",
            nickname = params["nickname"]?.toString() ?: "",
            avatar = params["avatar"]?.toString() ?: "",
            phone = params["phone"]?.toString() ?: "",
            email = params["email"]?.toString() ?: "",
            gender = params["gender"]?.toString() ?: "",
            signature = params["signature"]?.toString() ?: "",
            status = User.UserStatus.NORMAL,
            onlineStatus = User.OnlineStatus.ONLINE,
            lastActiveTime = LocalDateTime.now(),
            deletedAt = null,
            settings = settings,
            roles = params["roles"]?.toString() ?: "",
            token = ""
        )
        userEntity.createdAt = createdAt
        userEntity.updatedAt = updatedAt
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