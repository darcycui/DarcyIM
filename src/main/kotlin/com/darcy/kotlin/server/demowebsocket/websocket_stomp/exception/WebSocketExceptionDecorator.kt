package com.darcy.kotlin.server.demowebsocket.websocket_stomp.exception

import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.handler.WebSocketHandlerDecorator
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory
import java.io.IOException

/**
 * websocket 异常处理
 */
@Configuration
@EnableWebSocket
class WebSocketExceptionDecorator : WebSocketHandlerDecoratorFactory {
    override fun decorate(handler: WebSocketHandler): WebSocketHandler {
        return object : WebSocketHandlerDecorator(handler) {
            override fun afterConnectionEstablished(session: WebSocketSession) {
                super.afterConnectionEstablished(session)
                try {
                    DarcyLogger.info("websocket 连接已建立: ${session.id}")
                } catch (e: Exception) {
                    DarcyLogger.error("websocket 创建连接异常: ${session.id}", e)
                    e.printStackTrace()
                }
            }

            override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
                DarcyLogger.info("websocket 断开连接: ${session.id} 状态: $closeStatus")
                try {
                    super.afterConnectionClosed(session, closeStatus)
                } catch (e: Exception) {
                    if (ExceptionChecker.isIgnorableException(e)) {
                        // 忽略连接已关闭的异常
                        DarcyLogger.error("websocket 断开连接 忽略连接已关闭的异常: ${e::class.java.simpleName} ${e.message}")
                    } else {
                        DarcyLogger.error("websocket 断开连接异常: ${session.id}", e)
                        e.printStackTrace()
                    }
                }
            }

            override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
                DarcyLogger.info("websocket 处理消息: ${session.id}")
                try {
                    super.handleMessage(session, message)
                } catch (e: Exception) {
                    if (ExceptionChecker.isIgnorableException(e)) {
                        // 忽略连接已关闭的异常
                        DarcyLogger.error("websocket 处理消息 忽略连接已关闭的异常: ${e::class.java.simpleName} ${e.message}")
                    } else {
                        DarcyLogger.error("websocket 处理消息异常: ${session.id}", e)
                        e.printStackTrace()
                    }
                }
            }

            override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
                DarcyLogger.error("websocket 处理错误: ${session.id}", exception)
                try {
                    if (ExceptionChecker.isIgnorableException(exception)) {
                        // 忽略连接已关闭的异常
                        DarcyLogger.error("websocket 处理错误 忽略连接已关闭的异常: ${exception::class.java.simpleName} ${exception.message}")
                    } else {
                        super.handleTransportError(session, exception)
                    }
                } catch (e: Exception) {
                    DarcyLogger.error("websocket 处理错误异常: ${session.id}", e)
                    e.printStackTrace()
                }
            }
        }
    }

}
