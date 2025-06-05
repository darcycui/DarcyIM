package com.darcy.kotlin.server.demowebsocket.websocket.domain

import java.time.LocalDateTime

open class ChatEntityBase() {
    var from: String = ""
    var to: String = ""
    var createTime: LocalDateTime? = null
}
