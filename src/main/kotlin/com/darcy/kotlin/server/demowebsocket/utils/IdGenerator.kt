package com.darcy.kotlin.server.demowebsocket.utils

import org.springframework.stereotype.Component
import java.net.NetworkInterface
import java.security.SecureRandom
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

@Component
class IdGenerator {

    companion object {
        private const val CUSTOM_EPOCH = 1672531200000L // 2023-01-01T00:00:00Z
        private const val NODE_ID_BITS = 10L
        private const val SEQUENCE_BITS = 12L

        private const val MAX_NODE_ID = (1L shl NODE_ID_BITS.toInt()) - 1
        private const val MAX_SEQUENCE = (1L shl SEQUENCE_BITS.toInt()) - 1

        private const val NODE_ID_SHIFT = SEQUENCE_BITS
        private const val TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + NODE_ID_BITS
    }

    private val nodeId: Long = createNodeId()
    private var lastTimestamp = -1L
    private var sequence = AtomicLong(0)

    fun nextMessageId(): String = "msg_${nextId()}"

    fun nextGroupId(): String = "group_${nextId()}"

    fun nextConversationId(): String = "conv_${nextId()}"

    @Synchronized
    fun nextId(): Long {
        var timestamp = timeGen()

        if (timestamp < lastTimestamp) {
            throw IllegalStateException("时钟回拨异常")
        }

        if (lastTimestamp == timestamp) {
            sequence.set((sequence.get() + 1) and MAX_SEQUENCE)
            if (sequence.get() == 0L) {
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence.set(0)
        }

        lastTimestamp = timestamp

        return ((timestamp - CUSTOM_EPOCH) shl TIMESTAMP_LEFT_SHIFT.toInt()) or
                (nodeId shl NODE_ID_SHIFT.toInt()) or
                sequence.get()
    }

    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = timeGen()
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen()
        }
        return timestamp
    }

    private fun timeGen(): Long = System.currentTimeMillis()

    private fun createNodeId(): Long {
        return try {
            val sb = StringBuilder()
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val mac = networkInterface.hardwareAddress
                mac?.forEach { byte ->
                    sb.append("%02X".format(byte))
                }
            }
            (sb.toString().hashCode() and MAX_NODE_ID.toInt()).toLong()
        } catch (e: Exception) {
            SecureRandom().nextInt(MAX_NODE_ID.toInt()).toLong()
        }
    }
}