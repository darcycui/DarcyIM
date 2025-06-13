package com.darcy.kotlin.server.demowebsocket.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    @Value("\${upload.path}")
    private val picDir: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/file/**")
            .addResourceLocations("file:$picDir")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**") // 匹配所有以 /api 开头的路径
            .allowedOrigins("https://localhost:7443/") // 允许的来源
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
            .allowedHeaders("*") // 允许的头信息
            .allowCredentials(true) // 是否允许携带 Cookie
            .maxAge(30) // 预检请求的有效期（秒）
    }
}
