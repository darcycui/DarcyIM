package com.darcy.kotlin.server.demowebsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class DemoWebsocketApplication

fun main(args: Array<String>) {
    runApplication<DemoWebsocketApplication>(*args)
}
