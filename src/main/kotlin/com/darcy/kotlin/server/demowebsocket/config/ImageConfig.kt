package com.darcy.kotlin.server.demowebsocket.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


//@Configuration
//class ImageConfig : WebMvcConfigurer {
//    @Value("\${upload.path.image}")
//    private val picDir: String? = null
//
//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        registry.addResourceHandler("/file/**")
//            .addResourceLocations("file:$picDir")
//    }
//}