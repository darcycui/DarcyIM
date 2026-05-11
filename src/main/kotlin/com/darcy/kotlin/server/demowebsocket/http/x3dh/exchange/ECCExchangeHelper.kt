package com.darcy.kotlin.server.demowebsocket.http.x3dh.exchange

import java.security.*
import javax.crypto.KeyAgreement


object ECCExchangeHelper {
    // 初始化 指定使用 X25519 曲线
    private const val ALGORITHM: String = "X25519"

    fun generateKeyPair(): KeyPair {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
            return keyPairGenerator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun getSharedSecret(privateKey: PrivateKey?, publicKey: PublicKey?): ByteArray {
        try {
            val keyAgreement = KeyAgreement.getInstance(ALGORITHM)
            keyAgreement.init(privateKey)
            keyAgreement.doPhase(publicKey, true)
            return keyAgreement.generateSecret()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        }
    }

}