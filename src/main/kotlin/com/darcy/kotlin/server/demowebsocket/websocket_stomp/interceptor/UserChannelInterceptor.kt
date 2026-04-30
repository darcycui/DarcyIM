package com.darcy.kotlin.server.demowebsocket.websocket_stomp.interceptor

import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

/**
 * 拦截器 拦截用户名 注册到 STOMP
 */
@Component
class UserChannelInterceptor @Autowired constructor(
    @Lazy
    val simpUserRegistry: SimpUserRegistry
) : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor? = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
        accessor?.run {
            DarcyLogger.info("preSend command=${command?.name}")
            when (command) {
                StompCommand.CONNECT -> {
                    // 注册用户名
                    val userId = accessor.getNativeHeader("Authorization")?.get(0) ?: ""
                    setupUserNameForSTOMP(accessor, userId)
                    DarcyLogger.info("用户$userId 上线了")
                    userCount()
                }

                StompCommand.SUBSCRIBE -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("用户$userId 订阅了 $destination")
                }

                StompCommand.DISCONNECT -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("用户$userId 下线了")
                    userCount()
                }

                StompCommand.STOMP -> {}
                StompCommand.UNSUBSCRIBE -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("用户$userId 取消订阅了 $destination")
                }
                StompCommand.SEND -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("用户$userId 发送了消息到 $destination" )
                }
                StompCommand.ACK -> {}
                StompCommand.NACK -> {}
                StompCommand.BEGIN -> {}
                StompCommand.COMMIT -> {}
                StompCommand.ABORT -> {}
                StompCommand.CONNECTED -> {
                    userCount()
                }

                StompCommand.RECEIPT -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("用户$userId 接收了 RECEIPT 确认帧 $receiptId")
                }
                StompCommand.MESSAGE -> {}
                StompCommand.ERROR -> {
                    val userId = user?.name ?: ""
                    DarcyLogger.info("用户$userId 接收到错误帧 $sessionId")
                }
                else -> {}
            }
        }
        return message
    }

    private fun userCount() {
        val usersCount = simpUserRegistry.userCount
        DarcyLogger.info("当前在线人数为：$usersCount")
    }

    private fun setupUserNameForSTOMP(accessor: StompHeaderAccessor, userId: String) {
        if (userId.isEmpty() or userId.isBlank()) {
            DarcyLogger.error("用户未登录")
        }
        if (verifyUserId(userId)) {
            accessor.user = UserNamePrincipal(userId)
        }
    }

    private fun verifyUserId(userId: String): Boolean {
        return userId.startsWith("test")
    }
}