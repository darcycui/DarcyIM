package com.darcy.kotlin.server.demowebsocket.websocket.server

import com.alibaba.fastjson.JSONObject
import com.darcy.kotlin.server.demowebsocket.websocket.domain.ChatEntityStr
import com.darcy.kotlin.server.demowebsocket.websocket.helper.SendHelper
import jakarta.websocket.OnClose
import jakarta.websocket.OnError
import jakarta.websocket.OnMessage
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

/**
 * 每一个连接对应一个WebSocketServer对象
 */
@ServerEndpoint("/person/{userId}")
@Component
class WebSocketServer {
    companion object {
        /**
         * 存储所有连接 注意这个是map全局的要使用伴生对象
         */
        @JvmStatic
        private val SOCKET_MAP: MutableMap<String, WebSocketServer> = ConcurrentHashMap<String, WebSocketServer>()
    }

    private var session: Session? = null

    fun getSession(): Session? {
        return session
    }

    @OnOpen
    fun open(session: Session, @PathParam("userId") userId: String) {
        this.session = session
        SOCKET_MAP[userId] = this
        println("用户${userId}连接成功,当前连接总人数为${SOCKET_MAP.size}")
    }

    @OnClose
    fun close(session: Session, @PathParam("userId") userId: String) {
        SOCKET_MAP.remove(userId)
        println("用户${userId}断开连接,当前连接总人数为${SOCKET_MAP.size}")
    }

    @OnMessage
    fun onMessage(message: String, session: Session) {
        println("收到用户${session.id}的消息:$message")
        // fastjson 解析 message 为 ChatEntityStr
        val chatEntity = JSONObject.parseObject(message, ChatEntityStr::class.java)
        val socketServer = SOCKET_MAP[chatEntity.to]
        if (socketServer == null) {
            println("用户${chatEntity.to}不在线")
            return
        }
        SendHelper.sendToPersonStr(message, socketServer.session)
    }

    @OnError
    fun onError(session: Session, error: Throwable) {
        println("用户${session.id}发生错误")
        error.printStackTrace()
    }
}