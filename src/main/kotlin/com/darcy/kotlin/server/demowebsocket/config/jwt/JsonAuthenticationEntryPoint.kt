package com.darcy.kotlin.server.demowebsocket.config.jwt

import com.darcy.kotlin.server.demowebsocket.domain.error.ErrorEntity
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * 401 认证失败处理
 */
@Component
class JsonAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val errorEntity = ErrorEntity(
            status = HttpServletResponse.SC_UNAUTHORIZED,
            error = "Unauthorized",
            message = "认证失败: ${authException.message}",
            path = request.requestURI
        )

        response.writer.use {
            it.write(ObjectMapper().writeValueAsString(errorEntity))
        }
    }
}
