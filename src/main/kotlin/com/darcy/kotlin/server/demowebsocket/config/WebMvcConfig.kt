package com.darcy.kotlin.server.demowebsocket.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    @Value("\${upload.path.image}")
    private val picDir: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/file/**")
            .addResourceLocations("file:$picDir")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/api/**") // 匹配所有以 /api 开头的路径
        registry.addMapping("/**") // 匹配所有路径
//            .allowedOrigins("https://localhost:7443/", "https://10.0.0.241:7443/",
//                "http://localhost:7443/", "http://10.0.0.241:7443/", "null") // 允许的来源
            .allowedOriginPatterns("*", "null")
            .allowedMethods("*") // 允许的方法
            .allowedHeaders("*") // 允许的头信息
            .allowCredentials(true) // 是否允许携带 Cookie
            .maxAge(30) // 预检请求的有效期（秒）
    }
}
