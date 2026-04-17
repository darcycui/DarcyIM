package com.darcy.kotlin.server.demowebsocket.exception

/**
 * 参数相关异常
 * 601-699
 */
sealed class ParamsException(
    exceptionCode: Int,
    exceptionMessage: String,
    val params: Map<String, Any>
) : BaseException(exceptionCode, exceptionMessage) {
    data class ParamsError(
        val params2: Map<String, Any>
    ) : ParamsException(601, "参数错误", params2)

    data class ParamsNotExist(
        val params2: Map<String, Any>
    ) : ParamsException(602, "参数不存在", params2)

    data class ParamsNotValid(
        val params2: Map<String, Any>
    ) : ParamsException(603, "参数无效", params2)
}