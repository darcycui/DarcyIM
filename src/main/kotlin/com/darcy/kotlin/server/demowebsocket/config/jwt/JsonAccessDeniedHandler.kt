package com.darcy.kotlin.server.demowebsocket.config.jwt

import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.domain.error.ErrorEntity
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 * 403 拒绝访问处理
 */
@Component
class JsonAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN

        val errorEntity = ErrorEntity(
            status = HttpServletResponse.SC_FORBIDDEN,
            error = "Forbidden",
            message = "权限不足: ${accessDeniedException.message}",
            path = request.requestURI
        )

        response.writer.use {
            it.write(ObjectMapper().writeValueAsString(ResultEntity.success(errorEntity)))
        }
    }
}
