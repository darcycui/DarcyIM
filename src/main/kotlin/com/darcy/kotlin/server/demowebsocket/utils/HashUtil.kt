package com.darcy.kotlin.server.demowebsocket.utils

import java.security.MessageDigest

object HashUtil {
    fun sha256Str(original: String): String {
        val digest = sha256ByteArray(original.toByteArray())
//        return digest.joinToString("") { "%02x".format(it).lowercase() }
        return HexUtil.bytesToHexStr(digest)
    }

    fun sha256ByteArray(byteArray: ByteArray): ByteArray {
        println("original bytearray: ${byteArray.contentToString()}")
        if (byteArray.isEmpty()) return ByteArray(0)
        val hash = MessageDigest.getInstance("SHA-256")
        hash.update(byteArray)
        return hash.digest()
    }
}