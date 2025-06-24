//package com.darcy.kotlin.server.demowebsocket.websocket.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.web.socket.config.annotation.EnableWebSocket
//import org.springframework.web.socket.server.standard.ServerEndpointExporter
//
///**
// * 注入Configuration
// */
//@Configuration
//@EnableWebSocket
//class WebSocketConfig {
//
//    /**
//     * 注入ServerEndpointExporter Bean
//     * 该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
//     */
//    @Bean
//    fun serverEndpointExporter(): ServerEndpointExporter {
//        return ServerEndpointExporter()
//    }
//}