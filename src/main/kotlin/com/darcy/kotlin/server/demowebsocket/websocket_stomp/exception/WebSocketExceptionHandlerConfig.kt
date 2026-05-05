package com.darcy.kotlin.server.demowebsocket.websocket_stomp.exception

import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.handler.WebSocketHandlerDecorator
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory

@Configuration
@EnableWebSocket
class WebSocketExceptionHandlerConfig {

    @Bean
    fun webSocketHandlerDecoratorFactory(): WebSocketHandlerDecoratorFactory {
        return WebSocketHandlerDecoratorFactory { handler ->
            object : WebSocketHandlerDecorator(handler) {
                override fun afterConnectionEstablished(session: WebSocketSession) {
                    try {
                        super.afterConnectionEstablished(session)
                    } catch (e: Exception) {
                        DarcyLogger.error("WebSocket 连接建立失败: ${e.message}")
                    }
                }

                override fun handleMessage(session: WebSocketSession, message: org.springframework.web.socket.WebSocketMessage<*>) {
                    try {
                        super.handleMessage(session, message)
                    } catch (e: java.io.EOFException) {
                        DarcyLogger.warn("WebSocket 连接异常断开（EOF）: sessionId=${session.id}")
                    } catch (e: Exception) {
                        DarcyLogger.error("WebSocket 消息处理失败: ${e.message}")
                    }
                }

                override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
                    when (exception) {
                        is java.io.EOFException -> {
                            DarcyLogger.warn("WebSocket 传输错误 - 连接已关闭: sessionId=${session.id}")
                        }
                        else -> {
                            DarcyLogger.error("WebSocket 传输错误: sessionId=${session.id}, error=${exception.message}")
                        }
                    }
                }

                override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
                    try {
                        super.afterConnectionClosed(session, closeStatus)
                        DarcyLogger.info("WebSocket 连接关闭: sessionId=${session.id}, status=$closeStatus")
                    } catch (e: Exception) {
                        DarcyLogger.error("WebSocket 连接关闭处理失败: ${e.message}")
                    }
                }
            }
        }
    }
}
