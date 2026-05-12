package com.darcy.kotlin.server.demowebsocket.http.controller

import com.darcy.kotlin.server.demowebsocket.api.x3dh.IX3DHApi
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import com.darcy.kotlin.server.demowebsocket.exception.code100.UserException
import com.darcy.kotlin.server.demowebsocket.http.service.X3DHService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class X3DHApiController @Autowired constructor(
    private val x3dhService: X3DHService,
) : IX3DHApi {
    override fun pullBobKeys(params: Map<String, String>): String {
        val bobUserId = params["bobUserId"]?.toLong() ?: throw UserException.USER_NOT_EXIST
        val result = x3dhService.getBobPublicKeys(bobUserId)
        return ResultEntity.success(result).toJsonString()
    }
}