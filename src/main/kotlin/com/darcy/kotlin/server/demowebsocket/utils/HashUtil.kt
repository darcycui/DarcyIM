package com.darcy.kotlin.server.demowebsocket.utils

import java.security.MessageDigest

object HashUtil {
    fun sha256Str(original: String): String {
        val digest = sha256(original.toByteArray())
        return digest.joinToString("") { "%02x".format(it).lowercase() }
    }

    fun sha256(byteArray: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(byteArray)
    }
}