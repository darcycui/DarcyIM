package com.darcy.kotlin.server.demowebsocket.exception.code1000

import com.darcy.kotlin.server.demowebsocket.exception.BaseException

/**
 * 数据库相关异常
 * 1001-1099
 */
class X3DHException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val IDENTITY_KEY_NOT_EXIST = X3DHException(1001, "身份密钥不存在")
        val SIGNED_PRE_KEY_NOT_EXIST = X3DHException(1002, "签名密钥不存在")
        val ONE_TIME_PRE_KEY_NOT_EXIST = X3DHException(1003, "一次性密钥不存在")
        val DH_KEY_NOT_EXIST = X3DHException(1004, "DH密钥不存在")
    }
}