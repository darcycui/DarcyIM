package com.darcy.kotlin.server.demowebsocket.websocket.config

import jakarta.websocket.HandshakeResponse
import jakarta.websocket.server.HandshakeRequest
import jakarta.websocket.server.ServerEndpointConfig

class WsConfigurator : ServerEndpointConfig.Configurator() {
    override fun modifyHandshake(
        sec: ServerEndpointConfig,
        request: HandshakeRequest,
        response: HandshakeResponse
    ) {
        sec.userProperties["org.apache.tomcat.websocket.HEARTBEAT_INTERVAL"] = 30_000 // 30秒间隔
    }
}