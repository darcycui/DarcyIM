package com.darcy.kotlin.server.demowebsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoWebsocketApplication

fun main(args: Array<String>) {
    runApplication<DemoWebsocketApplication>(*args)
}
