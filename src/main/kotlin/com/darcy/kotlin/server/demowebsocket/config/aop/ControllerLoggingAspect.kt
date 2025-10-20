package com.darcy.kotlin.server.demowebsocket.config.aop

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class ControllerLoggingAspect {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val objectMapper = ObjectMapper()

    @Around("execution(* com.darcy.kotlin.server.demowebsocket.http.controller.*.*(..))")
    fun logControllerMethods(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val signature = joinPoint.signature as MethodSignature
        val methodName = "${signature.declaringType.simpleName}.${signature.name}"

        // 记录请求参数
        val params = getMethodParameters(joinPoint)
        logger.info("REQUEST: $methodName | Params: {}", params)

        return try {
            // 执行方法
            val result = joinPoint.proceed()

            // 记录响应结果
            logger.info("RESPONSE: $methodName | Result: {} | Time: {}ms",
                toJsonString(result), System.currentTimeMillis() - startTime)

            result
        } catch (e: Exception) {
            logger.error("ERROR in $methodName | {}", e.message)
            throw e
        }
    }

    private fun getMethodParameters(joinPoint: ProceedingJoinPoint): String {
        val signature = joinPoint.signature as MethodSignature
        val parameterNames = signature.parameterNames
        val args = joinPoint.args
        val params = mutableMapOf<String, Any?>()

        for (i in args.indices) {
            // 忽略HttpServletRequest, HttpServletResponse等特殊参数
            if (args[i] !is HttpServletRequest && args[i] !is HttpServletResponse) {
                params[parameterNames[i]] = args[i]
            }
        }

        return toJsonString(params)
    }

    private fun toJsonString(obj: Any?): String {
        return try {
            objectMapper.writeValueAsString(obj)
        } catch (e: Exception) {
            "JSON_ERROR: ${e.message}"
        }
    }
}
