package com.darcy.kotlin.server.demowebsocket.websocket.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/*/*")
            .allowedOrigins("http://localhost:8080")
            .allowedMethods("GET", "POST")
            .allowCredentials(true)
    }
}