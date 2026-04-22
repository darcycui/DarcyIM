package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class LocalDateTimeTests {
    @Test
    fun `test-LocalDateTime-now`() {
//        val now = LocalDateTime.now()
        val now =  LocalDateTime.now(ZoneId.of("Asia/Shanghai"))
//        val now =  LocalDateTime.now(ZoneId.of("America/New_York"))
        println("now=$now")
        println("now.toString()=${now.toString()}")
        val formatNowISO = TimeUtil.formatDateTimeToString( now)
        println("formatNowISO=${formatNowISO}")
    }

    @Test
    fun `test-LocalDateTime-parse`() {
        val now = LocalDateTime.now()
        val timeStr = now.toString()
        val format = TimeUtil.parseStringToDateTime(timeStr)
        println(format)
    }
}