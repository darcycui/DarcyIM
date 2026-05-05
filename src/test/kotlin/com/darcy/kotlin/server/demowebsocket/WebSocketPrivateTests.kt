package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.domain.dto.message.PrivateMessageDTO
import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.lang.Nullable
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebSocketPrivateTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun createJwtToken1(): String {
        return "test1"
    }

    private fun createJwtToken2(): String {
        return "test2"
    }

    private fun createWebSocketStompClientSession(jwtToken: String): StompSession {
        // 1. 创建客户端
        val transports = listOf(WebSocketTransport(StandardWebSocketClient()))
        val sockJsClient = SockJsClient(transports)
        val stompClient = WebSocketStompClient(sockJsClient)
        // 关键：配置 TaskScheduler
        val taskScheduler = ThreadPoolTaskScheduler().apply {
            poolSize = 4
//            threadNamePrefix = "websocket-receipt-scheduler-"
            isDaemon = true
            initialize()
        }
        stompClient.taskScheduler = taskScheduler
        // 设置消息转换器
        val converter = MappingJackson2MessageConverter()
        stompClient.messageConverter = converter
        // 开启底层调试
        stompClient.receiptTimeLimit = 10_000  // RECEIPT 超时时间（如果你的版本支持）

        // 2. 准备头部
        val webSocketHeaders = WebSocketHttpHeaders()
        webSocketHeaders["Authorization"] = jwtToken
        val stompHeaders = StompHeaders()
        // 可以设置 STOMP 特定的头部
        stompHeaders["Authorization"] = jwtToken
        val url = "http://localhost:$port/stomp-ws"  // 注意 SockJS 用 http
        val sessionFuture = CompletableFuture<StompSession>()

        // 3. 使用正确的 connectAsync 重载
        stompClient.connectAsync(url, webSocketHeaders, stompHeaders, object : StompSessionHandlerAdapter() {
//            override fun getPayloadType(headers: StompHeaders): Type {
//                return PrivateMessageDTO::class.java
//            }

            override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                println("Connected! Session: ${session.sessionId}")
                println("Connected headers: $connectedHeaders")
                sessionFuture.complete(session)
            }

            override fun handleException(
                session: StompSession,
                @Nullable command: StompCommand?,
                headers: StompHeaders,
                payload: ByteArray,
                exception: Throwable
            ) {
                println("Exception: ${exception.message}")
            }

            override fun handleTransportError(session: StompSession, exception: Throwable) {
                println("Transport error: ${exception.message}")
            }
        })

        val session = sessionFuture.get(5, TimeUnit.SECONDS)
        return session
    }

    // STOMP 是 Simple Text Oriented Messaging Protocol
    // 简单文本定向消息协议。
    @Test
    fun `test-private-websocket-stomp`() {
        // 创建两个会话
        val session1 = createWebSocketStompClientSession(createJwtToken1())
        val session2 = createWebSocketStompClientSession(createJwtToken2())

        // 设置 receipt 确认帧
        session1.setAutoReceipt(true)
        session2.setAutoReceipt(true)

        // 4. 等待连接
        try {

            // 使用 CountDownLatch 等待消息接收
            val messageLatch = CountDownLatch(1)
            val receiptLatch = CountDownLatch(1)

            // 5. 订阅主题
            val queueSubscription = session1.subscribe("/user/queue/message", object : StompSessionHandlerAdapter() {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    println("1--Received Queue message-->: $payload")
                }

                override fun getPayloadType(headers: StompHeaders): Type {
                    return PrivateMessageDTO::class.java
                }
            })
            queueSubscription.addReceiptTask(Runnable {
                println("🎉 [RECEIPT SUCCESS] 订阅1的确认帧已被服务器接收!")
            })
            queueSubscription.addReceiptLostTask {
                println("❌ [RECEIPT FAILURE] 订阅1的确认帧失败!")
            }
            Thread.sleep(500) // 等待订阅确认日志

            val queueSubscription2 = session2.subscribe("/user/queue/message", object : StompSessionHandlerAdapter() {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    println("2--Received Queue message-->: $payload")
                    // 收到消息后 调用 messageLatch 的 countDown
                    messageLatch.countDown()
                }

                override fun getPayloadType(headers: StompHeaders): Type {
                    return PrivateMessageDTO::class.java
                }
            })
            queueSubscription2.addReceiptTask(Runnable {
                println("🎉 [RECEIPT SUCCESS] 订阅2的确认帧已被服务器接收!")
            })
            queueSubscription2.addReceiptLostTask {
                println("❌ [RECEIPT FAILURE] 订阅2的确认帧失败!")
            }
            Thread.sleep(500) // 等待订阅确认日志

            // 6. 发送消息
            val sendHeaders = StompHeaders().apply {
                destination = "/app/sendPrivateMessage"
                receipt = "receipt-1${System.currentTimeMillis()}"
//                receiptId = "receipt-1${System.currentTimeMillis()}"
            }
            val privateMessage = PrivateMessageDTO(
                msgId = "",
                senderId = 14,
                senderName = "test1",
                receiverId = 13,
                receiverName = "test2",
                content = "测试消息1",
                msgType = "TEXT",
                sendTime = TimeUtil.getCurrentTimeString(),
                isRead = false,
                isRecalled = false
            )
            // 发送消息 获取返回值用于处理Receipt确认帧
            val sendReceipt = session1.send(sendHeaders, privateMessage)
            // *** 核心：注册异步接收任务以记录日志 ***
            sendReceipt.addReceiptTask(Runnable {
                println("🎉 [RECEIPT SUCCESS] 发送操作成功! Receipt ID: ${sendReceipt.receiptId}")
                receiptLatch.countDown()
            })
            sendReceipt.addReceiptLostTask {
                println("❌ [RECEIPT FAILURE] 发送操作失败! Receipt ID: ${sendReceipt.receiptId}")
            }

            // 3. 等待消息接收与确认帧结果
            // 等待消息接收（最多等待 5 秒）
            val received = messageLatch.await(5, TimeUnit.SECONDS)
            if (received) {
                println("✓ 测试通过：成功接收消息")
            } else {
                println("✗ 测试失败：未在超时时间内接收到消息")
            }
            assertEquals(true, received, "接收消息错误")

            val receiptReceived = receiptLatch.await(10, TimeUnit.SECONDS)
            if (receiptReceived) {
                println("✓ 测试通过：成功接收消息与确认帧")
            } else {
                println("✗ 测试失败：未在超时时间内收到确认帧，但消息已成功投递")
            }
            assertEquals(true, receiptReceived, "未在超时时间内收到确认帧，但消息已成功投递")

            // 7. 清理
            queueSubscription.unsubscribe()
            queueSubscription2.unsubscribe()
            session1.disconnect()
            session2.disconnect()
            println("✓ Disconnected")

        } catch (e: TimeoutException) {
            e.printStackTrace()
            println("✗ Connection timeout")
        }
    }

}