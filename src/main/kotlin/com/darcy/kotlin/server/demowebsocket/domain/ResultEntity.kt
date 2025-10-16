package com.darcy.kotlin.server.demowebsocket.domain

import com.alibaba.fastjson.JSONObject
import com.darcy.kotlin.server.demowebsocket.domain.error.ErrorEntiry
import com.darcy.kotlin.server.demowebsocket.exception.BaseException
import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger

/**
 * {
 *     "resultcode": "200",
 *     "reason": "success",
 *     "result": {
 *         "Country": "中国",
 *         "Province": "",
 *         "City": "",
 *         "District": "",
 *         "Isp": "Zenlayer Inc"
 *     },
 *     "error_code": 0
 * }
 */
class ResultEntity<T>() {
    var resultcode: Int = 200
    var reason: String = ""
    var result: T? = null
    var error_code: Int = -1

    companion object {
        fun <T> success(data: T?): ResultEntity<T> {
            return ResultEntity<T>().apply {
                this.resultcode = 200
                this.result = data
            }
        }

        fun error(errorEntity: ErrorEntiry): ResultEntity<ErrorEntiry> {
            return ResultEntity<ErrorEntiry>().apply {
                this.resultcode = errorEntity.status
                this.reason = errorEntity.message
                this.error_code = errorEntity.status
                this.result = errorEntity
            }
        }

        fun error(e: BaseException): ResultEntity<ErrorEntiry> {
            val errorEntity = ErrorEntiry(
                status = e.exceptionCode,
                message = e.exceptionMessage
            )
            return error(errorEntity)
        }
    }

    fun toJsonString(): String {
        return JSONObject.toJSONString(this).also { DarcyLogger.warn(it) }
    }
}
