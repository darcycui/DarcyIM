package com.darcy.kotlin.server.demowebsocket.utils

import com.darcy.kotlin.server.demowebsocket.log.DarcyLogger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {
    private const val TIME_FORMATTER: String = "yyyy-MM-dd HH:mm:ss"

    fun getCurrentTimeStamp(): String {
        val formatter = DateTimeFormatter.ofPattern(TIME_FORMATTER)
        return LocalDateTime.now().format(formatter)
    }


    fun parseDataTime(value: Any?): LocalDateTime {
        return when (value) {
            is LocalDateTime -> value.also {
                DarcyLogger.info("parseCreatedAt is LocalDateTime")
            }

            is String -> parseStringToDateTime(value).also {
                DarcyLogger.info("parseCreatedAt is String")
            }

            else -> LocalDateTime.now().also {
                DarcyLogger.info("parseCreatedAt is else")
            }
        }
    }

    fun parseStringToDateTime(dateStr: String): LocalDateTime {
        val formats = listOf(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        )

        for (format in formats) {
            try {
                return LocalDateTime.parse(dateStr, format)
            } catch (e: Exception) {
                continue
            }
        }
        DarcyLogger.debug("无法解析日期时间格式: $dateStr")
        return LocalDateTime.of(1970, 1, 1, 0, 0, 0)
    }
}