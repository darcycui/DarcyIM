package com.darcy.kotlin.server.demowebsocket.exception

/**
 * 文件相关异常
 * 201-299
 */
class FileException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val FILE_SAVE_FAILED = FileException(201, "文件保存失败")
        val FILE_NOT_FOUND = FileException(202, "文件不存在")
    }
}