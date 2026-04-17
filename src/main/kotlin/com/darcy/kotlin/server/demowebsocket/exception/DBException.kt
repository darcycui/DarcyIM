package com.darcy.kotlin.server.demowebsocket.exception

/**
 * 数据库相关异常
 * 301-399
 */
class DBException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val DB_SAVE_FAILURE = DBException(301, "数据库保存失败")
    }
}