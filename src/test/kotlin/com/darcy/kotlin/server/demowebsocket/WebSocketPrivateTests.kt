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
        // 设置消息转换器
        val converter = MappingJackson2MessageConverter()
        stompClient.messageConverter = converter

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

        // 4. 等待连接
        try {

            // 使用 CountDownLatch 等待消息接收
            val messageLatch = CountDownLatch(1)

            // 5. 订阅主题
            val queueSubscription = session1.subscribe("/user/queue/message", object : StompSessionHandlerAdapter() {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    println("1--Received Queue message-->: $payload")
                }

                override fun getPayloadType(headers: StompHeaders): Type {
                    return PrivateMessageDTO::class.java
                }
            })

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

            // 6. 发送消息
            val sendHeaders = StompHeaders().apply {
                destination = "/app/sendPrivateMessage"
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
            session1.send(sendHeaders, privateMessage)

            // 等待
            Thread.sleep(3_000)
            // 等待消息接收（最多等待 5 秒）
            val received = messageLatch.await(5, TimeUnit.SECONDS)
            if (received) {
                println("✓ Test passed: Message received successfully")
            } else {
                println("✗ Test failed: Message not received within timeout")
            }
            assertEquals(true, received, "接收消息错误")

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