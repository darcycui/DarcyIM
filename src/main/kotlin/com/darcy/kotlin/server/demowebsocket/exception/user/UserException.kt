package com.darcy.kotlin.server.demowebsocket.exception.user

import com.darcy.kotlin.server.demowebsocket.exception.BaseException

class UserException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val USER_NOT_EXIST = UserException(101, "用户不存在")
        val USER_NAME_PASSWORD_ERROR = UserException(102, "用户名或密码错误")
        val USER_NAME_PASSWORD_EMPTY = UserException(103, "用户名或密码不能为空")
        val USER_DELETE_ERROR = UserException(104, "用户删除失败")
        val USER_UPDATE_ERROR = UserException(105, "用户更新失败")
        val USER_NAME_ALREADY_EXIST = UserException(106, "用户名已存在")
        val USER_NAME_EMPTY = UserException(106, "用户名不能为空")
    }

}