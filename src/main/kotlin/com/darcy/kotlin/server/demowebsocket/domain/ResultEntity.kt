package com.darcy.kotlin.server.demowebsocket.domain

import com.alibaba.fastjson.JSONObject
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
    var resultcode: Int = -1
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

        fun <T> error(code: Int, message: String): ResultEntity<T> {
            return ResultEntity<T>().apply {
                this.resultcode = 500
                this.reason = message
                this.error_code = code
            }
        }

        fun <T> error(e: BaseException): ResultEntity<T> {
            return return ResultEntity<T>().apply {
                this.resultcode = 500
                this.reason = e.exceptionMessage
                this.error_code = e.exceptionCode
            }
        }
    }

    fun toJsonString(): String {
        return JSONObject.toJSONString(this).also { DarcyLogger.warn(it) }
    }
}
