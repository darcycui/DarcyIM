package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api/upload")
interface IUploadFileApi {
    @PostMapping("/image")
    fun uploadImage(
        @RequestParam("userId") userId: Long,
        @RequestParam("file") file: MultipartFile
    ): String
}