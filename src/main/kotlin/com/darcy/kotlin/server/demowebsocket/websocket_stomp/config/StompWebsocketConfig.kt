package com.darcy.kotlin.server.demowebsocket.websocket_stomp.config

import com.darcy.kotlin.server.demowebsocket.websocket_stomp.interceptor.UserChannelInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration

@Configuration
@EnableWebSocketMessageBroker
class StompWebsocketConfig @Autowired constructor(
    val userChannelInterceptor: UserChannelInterceptor
) : WebSocketMessageBrokerConfigurer {
    companion object {
        private const val WEBSOCKET_PATH = "/stomp-ws"
        private const val HEARTBEAT_PERIOD = 60_000L

        private const val SUBSCRIBE_GROUP_MESSAGE_PREFIX = "/topic"
        private const val SUBSCRIBE_SINGLE_MESSAGE_PREFIX = "/queue"
        private const val CLIENT_SEND_MESSAGE_PREFIX = "/app"
        private const val SERVER_END_MESSAGE_PREFIX = "/user"
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        super.configureMessageBroker(registry)
        registry.apply {
            // 订阅路径前缀
            enableSimpleBroker(SUBSCRIBE_GROUP_MESSAGE_PREFIX, SUBSCRIBE_SINGLE_MESSAGE_PREFIX)
            // 单聊: client 发送消息前缀
            setApplicationDestinationPrefixes(CLIENT_SEND_MESSAGE_PREFIX)
            // 单聊: server 发送消息前缀
            setUserDestinationPrefix(SERVER_END_MESSAGE_PREFIX)
        }
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        super.registerStompEndpoints(registry)
        registry.apply {
            // 添加原生 STOMP 端点
            addEndpoint(WEBSOCKET_PATH)
                .setAllowedOriginPatterns("*", "null")

            // 添加 STOMP 端点，并开启 SockJS 支持
            addEndpoint(WEBSOCKET_PATH)
                .setAllowedOriginPatterns("*", "null")
                .withSockJS()
                .setHeartbeatTime(HEARTBEAT_PERIOD)
        }
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        super.configureClientInboundChannel(registration)
        registration.interceptors(userChannelInterceptor)
    }

    override fun configureClientOutboundChannel(registration: ChannelRegistration) {
        super.configureClientOutboundChannel(registration)
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        return super.configureMessageConverters(messageConverters)
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        super.configureWebSocketTransport(registry)
    }
}