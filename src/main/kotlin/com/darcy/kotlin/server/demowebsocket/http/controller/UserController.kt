package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.IUserApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.UserService
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class UserController @Autowired constructor(val userService: UserService) : IUserApi {

    override fun createUser(params: Map<String, Any>): String {
        val settings = if (params.containsKey("settings")) {
            @Suppress("UNCHECKED_CAST")
            params["settings"] as? Map<String, Any> ?: emptyMap()
        } else {
            emptyMap()
        }

        val createdAt = parseCreatedAt(params["createdAt"])
        val updatedAt = parseCreatedAt(params["updatedAt"])

        val userEntity = User(
            username = params["name"]?.toString() ?: "",
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

    private fun parseCreatedAt(value: Any?): LocalDateTime {
        return when (value) {
            is LocalDateTime -> value
            is String -> parseDateTimeString(value)
            else -> LocalDateTime.now()
        }
    }


    private fun parseDateTimeString(dateStr: String): LocalDateTime {
        val formats = listOf(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        )

        for (format in formats) {
            try {
                return LocalDateTime.parse(dateStr, format)
            } catch (e: Exception) {
                continue
            }
        }

        throw IllegalArgumentException("无法解析日期时间格式: $dateStr")
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