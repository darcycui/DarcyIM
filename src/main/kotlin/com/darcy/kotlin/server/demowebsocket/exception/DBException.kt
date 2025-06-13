package com.darcy.kotlin.server.demowebsocket.exception

class DBException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val DB_SAVE_FAILURE = DBException(301, "数据库保存失败")
    }
}