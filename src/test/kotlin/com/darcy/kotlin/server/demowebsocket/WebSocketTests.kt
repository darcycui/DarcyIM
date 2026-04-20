package com.darcy.kotlin.server.demowebsocket

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
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebSocketTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    // 模拟MVC
    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun createJwtToken(): String {
        return "test1"
    }

    // STOMP 是 Simple Text Oriented Messaging Protocol
    // 简单文本定向消息协议。
    @Test
    fun `test-connect-websocket-stomp`() {
        // 1. 创建客户端
        val transports = listOf(WebSocketTransport(StandardWebSocketClient()))
        val sockJsClient = SockJsClient(transports)
        val stompClient = WebSocketStompClient(sockJsClient)

        // 设置消息转换器
        val converter = MappingJackson2MessageConverter()
        stompClient.messageConverter = converter

        // 2. 准备头部
        val webSocketHeaders = WebSocketHttpHeaders()
        webSocketHeaders["Authorization"] = createJwtToken()

        val stompHeaders = StompHeaders()
        // 可以设置 STOMP 特定的头部
        stompHeaders["Authorization"] = createJwtToken()

        val url = "http://localhost:$port/stomp-ws"  // 注意 SockJS 用 http

        val sessionFuture = CompletableFuture<StompSession>()

        // 3. 使用正确的 connectAsync 重载
        stompClient.connectAsync(url, webSocketHeaders, stompHeaders, object : StompSessionHandlerAdapter() {
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

        // 4. 等待连接
        try {
            val session = sessionFuture.get(5, TimeUnit.SECONDS)

            // 5. 订阅主题
            val subscription = session.subscribe("/topic/messages", object : StompSessionHandlerAdapter() {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    println("Received message: $payload")
                }
            })

            // 6. 发送消息
            val sendHeaders = StompHeaders().apply {
                destination = "/app/send"
            }
            session.send(sendHeaders, "Test message")

            Thread.sleep(2000)

            // 7. 清理
            subscription.unsubscribe()
            session.disconnect()

        } catch (e: TimeoutException) {
            println("Connection timeout")
        }
    }

}