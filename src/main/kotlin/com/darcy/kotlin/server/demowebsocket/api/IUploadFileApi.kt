package com.darcy.kotlin.server.demowebsocket.api

import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api")
interface IUploadFileApi {
    @PostMapping("/upload/image")
    fun uploadImage(
        @RequestParam("userId") userId: Long,
        @RequestParam("file") file: MultipartFile
    ): String

    @GetMapping("/download/image/{fileName}")
    fun downloadImage(
        @PathVariable("fileName") fileName: String
    ): ResponseEntity<Resource>

    @GetMapping("/list/images")
    fun getAllImages(): String
}