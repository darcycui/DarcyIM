package com.darcy.kotlin.server.demowebsocket.utils

object HexUtil {
    fun bytes2Hex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun hex2Bytes(hex: String): ByteArray {
        return hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}