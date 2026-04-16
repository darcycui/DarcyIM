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
//        val now =  LocalDateTime.now(ZoneId.of("Asia/Shanghai"))
        val now =  LocalDateTime.now(ZoneId.of("America/New_York"))
        println(now)
        println(now.toString())
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatNow = now.format(format)
        println(formatNow)
    }

    @Test
    fun `test-LocalDateTime-parse`() {
        val now = LocalDateTime.now()
        val timeStr = now.toString()
        val format = TimeUtil.parseDataTime(timeStr)
        println(format)
    }
}