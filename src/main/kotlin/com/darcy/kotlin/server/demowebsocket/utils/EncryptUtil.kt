package com.darcy.kotlin.server.demowebsocket.utils

import java.nio.charset.StandardCharsets
import java.security.Key

object EncryptUtil {

    fun appendArrays(vararg arrays: ByteArray): ByteArray {
        var length = 0
        for (array in arrays) {
            length += array.size
        }
        val result = ByteArray(length)
        var pos = 0
        for (array in arrays) {
            System.arraycopy(array, 0, result, pos, array.size)
            pos += array.size
        }
        return result
    }

    fun splitArray64(array: ByteArray, subArrayLength: Int): Pair<ByteArray, ByteArray> {
        val result = Pair(ByteArray(subArrayLength), ByteArray(subArrayLength))
        System.arraycopy(array, 0, result.first, 0, subArrayLength)
        System.arraycopy(array, subArrayLength, result.second, 0, subArrayLength)
        return result
    }

    fun splitArray80(
        array: ByteArray,
        keyLength: Int,
        macLength: Int,
        ivLength: Int
    ): Triple<ByteArray, ByteArray, ByteArray> {
        val key = ByteArray(keyLength)
        val mac = ByteArray(macLength)
        val iv = ByteArray(ivLength)
        System.arraycopy(array, 0, key, 0, keyLength)
        System.arraycopy(array, keyLength, mac, 0, macLength)
        System.arraycopy(array, keyLength + macLength, iv, 0, ivLength)
        return Triple(key, mac, iv)
    }

    fun log(info: String, key: Key) {
        val hexString: String = HexUtil.bytesToHexStr(key.encoded)
        println("$info: $hexString")
    }

    fun log(info: String, bytes: ByteArray?) {
        val hexString: String = HexUtil.bytesToHexStr(bytes)
        println("$info: $hexString")
    }

    fun toNormalString(bytes: ByteArray?): String {
        return String(bytes!!, StandardCharsets.UTF_8)
    }
}