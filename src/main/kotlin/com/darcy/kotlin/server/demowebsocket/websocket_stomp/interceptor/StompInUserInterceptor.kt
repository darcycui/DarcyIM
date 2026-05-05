package com.darcy.kotlin.server.demowebsocket.websocket_stomp.interceptor

import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import java.lang.Exception

/**
 * In拦截器 拦截服务端收到的消息
 */
@Component
class StompInUserInterceptor @Autowired constructor(
    @Lazy
    val simpUserRegistry: SimpUserRegistry
) : ChannelInterceptor, ApplicationListener<SessionConnectedEvent> {
    companion object {
        private const val TAG = "StompInUserInterceptor"
    }
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
//        val accessor = StompHeaderAccessor.wrap(message)
        val accessor = MessageHeaderAccessor.getAccessor(
            message, StompHeaderAccessor::class.java
        ) ?: return message
        accessor.run {
            DarcyLogger.info("$TAG preSend command=${command?.name}")
            when (command) {
                StompCommand.CONNECT -> {
                    // websocket connect 方式连接 注册用户名
                    registerUserId(accessor)
                }

                StompCommand.STOMP -> {
                    // websocket STOMP 方式连接 注册用户名
                    registerUserId(accessor)
                }

                StompCommand.SUBSCRIBE -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("$TAG 用户$userId 订阅了 $destination")
                }

                StompCommand.DISCONNECT -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("$TAG 用户$userId 下线了")
                    userCount()
                }

                StompCommand.UNSUBSCRIBE -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("$TAG 用户$userId 取消订阅了 $destination")
                }

                StompCommand.SEND -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("$TAG 用户$userId 发送了消息到 $destination")
                }

                StompCommand.ACK -> {}
                StompCommand.NACK -> {}
                StompCommand.BEGIN -> {}
                StompCommand.COMMIT -> {}
                StompCommand.ABORT -> {}

                StompCommand.RECEIPT -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("$TAG 用户$userId 接收了 RECEIPT 确认帧 $receiptId")
                }

                StompCommand.MESSAGE -> {}
                StompCommand.ERROR -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("$TAG 用户$userId 接收到错误帧 $sessionId")
                }

                else -> {}
            }
        }
        return message
    }

    override fun afterSendCompletion(message: Message<*>, channel: MessageChannel, sent: Boolean, ex: Exception?) {
        super.afterSendCompletion(message, channel, sent, ex)
        if (ex != null) {
            val accessor = StompHeaderAccessor.wrap(message)
            DarcyLogger.error("$TAG afterSendCompletion 异常: " +
                    "command=${accessor.command}, " +
                    "sessionId=${accessor.sessionId}", ex)
        }
    }

    private fun registerUserId(accessor: StompHeaderAccessor) {
        // 1. 先从 STOMP 头中获取 Authorization
        var userId = accessor.getFirstNativeHeader("Authorization") ?: ""
        DarcyLogger.info("$TAG 从STOMP头获取 userId: $userId")

        // 2. 如果 STOMP 头中没有，则从 WebSocket 握手阶段的 sessionAttributes 中获取
        val sessionAttributes = accessor.sessionAttributes ?: emptyMap()
        val userId2 = sessionAttributes["userId"]?.toString() ?: ""
        DarcyLogger.info("$TAG 从握手属性获取 userId2: $userId2")
        if (userId.isBlank()) {
            userId = userId2
        }
        setupUserNameForSTOMP(accessor, userId)
        DarcyLogger.info("$TAG 用户$userId 上线了")
    }

    private fun userCount() {
        val usersCount = simpUserRegistry.userCount
        DarcyLogger.info("$TAG 当前在线人数为：$usersCount")
    }

    private fun setupUserNameForSTOMP(accessor: StompHeaderAccessor, userId: String) {
        if (userId.isEmpty() or userId.isBlank()) {
            DarcyLogger.error("$TAG 用户未登录")
        }
        if (verifyUserId(userId).not()) {
            DarcyLogger.error("$TAG 用户userId：$userId 不合法")
        }
        accessor.user = UserNamePrincipal(userId)
    }

    private fun verifyUserId(userId: String): Boolean {
        // TODO: 验证用户是否合法 token JWT 验证
        return userId.isNotEmpty()
//        return userId.startsWith("test")
    }

    override fun onApplicationEvent(event: SessionConnectedEvent) {
        val sessionId = event.message.headers["simpSessionId"]?.toString() ?: ""
        val user = event.user?.name ?: ""
        DarcyLogger.info("$TAG SessionConnectedEvent: 用户 $user 已完成连接，sessionId: $sessionId")
//        userCount()
    }
}