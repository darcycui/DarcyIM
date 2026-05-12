package com.darcy.kotlin.server.demowebsocket.utils

import com.darcy.kotlin.server.demowebsocket.http.x3dh.exchange.ECCExchangeHelper.ALGORITHM
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object KeyUtil {
    fun bytesToPrivateKey(privateKeyBytes: ByteArray): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        return keyFactory.generatePrivate(keySpec)
    }

    fun bytesToPublicKey(publicKeyBytes: ByteArray): PublicKey {
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
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