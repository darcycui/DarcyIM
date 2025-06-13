package com.darcy.kotlin.server.demowebsocket.exception

class FileException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val FILE_SAVE_FAILED = FileException(201, "文件保存失败")
    }
}