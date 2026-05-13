package com.darcy.kotlin.server.demowebsocket.utils

import com.alibaba.fastjson2.JSON
import com.darcy.kotlin.server.demowebsocket.http.x3dh.exchange.ECCExchangeHelper.ALGORITHM
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

const val PRIVATE_KEY_PKCS8_PREFIX = "302e020100300506032b656e04220420"
const val PUBLIC_KEY_X509_PREFIX = "302a300506032b656e032100"

object KeyUtil {
    fun bytesToPrivateKey(privateKeyBytes: ByteArray): PrivateKey {
        println("privateKeyBytes长度: ${privateKeyBytes.size}")
        val prefixBytes = PRIVATE_KEY_PKCS8_PREFIX.hexStrToBytes()
        val keySpec = PKCS8EncodedKeySpec(prefixBytes + privateKeyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        return keyFactory.generatePrivate(keySpec)
    }

    fun bytesToPublicKey(publicKeyBytes: ByteArray): PublicKey {
        println("publicKeyBytes长度: ${publicKeyBytes.size}")
        val prefixBytes = PUBLIC_KEY_X509_PREFIX.hexStrToBytes()
        val keySpec = X509EncodedKeySpec(prefixBytes + publicKeyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        return keyFactory.generatePublic(keySpec)
    }
}

fun ByteArray.toPrivateKey(): PrivateKey {
    return KeyUtil.bytesToPrivateKey(this)
}

fun ByteArray.toPublicKey(): PublicKey {
    return KeyUtil.bytesToPublicKey(this)
}

fun Key.keyToString(): String {
    val totalBytes = this.encoded
    val keyBytes = if (this is PrivateKey) {
        totalBytes.drop(PRIVATE_KEY_PKCS8_PREFIX.hexStrToBytes().size)
    } else {
        totalBytes.drop(PUBLIC_KEY_X509_PREFIX.hexStrToBytes().size)
    }
    return HexUtil.bytesToHexStr(keyBytes.toByteArray())
}

fun List<Key>.keysToString(): String {
    val hexStrings = this.map {
        it.keyToString()
    }
    return JSON.toJSONString(hexStrings)
}