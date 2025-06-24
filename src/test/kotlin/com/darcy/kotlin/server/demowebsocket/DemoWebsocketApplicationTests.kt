package com.darcy.kotlin.server.demowebsocket

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

// 随机端口 用于单元测试
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoWebsocketApplicationTests {

    // 注入随机端口
    @LocalServerPort
    private var port: Int = 0 // 注入随机端口

    @Test
    fun testStompClientConnection() {
        // 创建 STOMP 客户端
        val webSocketClient = StandardWebSocketClient()
//        val webSocketClient = SockJsClient(listOf(WebSocketTransport(StandardWebSocketClient())))
        val stompClient = WebSocketStompClient(webSocketClient)
        stompClient.start()

        // 连接控制
        val connectionLatch = CountDownLatch(1)
        var connected = false

        // 会话处理器
        val sessionHandler = object : StompSessionHandlerAdapter() {
            override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                connected = true
                session.send("/app/chat", """
                    {"from": "test3", 'text': message from test3, 'recipient': 'test2'}
                """.trimIndent()); // 发送测试消息
                connectionLatch.countDown()
            }

            // 添加错误处理
            override fun handleTransportError(session: StompSession, exception: Throwable) {
                println("Transport error: ${exception.message}")
                exception.printStackTrace() // 打印完整堆栈
                connectionLatch.countDown()
            }
        }

        // 发起连接
        val connectUrl = "ws://localhost:$port/stomp-ws"
        println("URL-->$connectUrl")
        val connectHeaders = StompHeaders().apply {
            // 添加认证
            set("Authorization", "test3")
            // 添加 Origin 头
            set("Origin", "http://localhost:$port")
        }
        stompClient.connectAsync(connectUrl, null, connectHeaders, sessionHandler)

        // 等待连接结果（最多5秒）
        connectionLatch.await(10, TimeUnit.SECONDS)

        // 断言连接成功
        assertTrue(connected, "STOMP client failed to connect to $connectUrl")
    }
}
