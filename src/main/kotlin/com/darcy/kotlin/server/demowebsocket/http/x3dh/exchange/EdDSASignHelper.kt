package com.darcy.kotlin.server.demowebsocket.http.x3dh.exchange

import java.security.*


object EdDSASignHelper {
    private const val ALGORITHM_EDDSA: String = "Ed25519" // 椭圆曲线算法 EdDSA
    private const val ALGORITHM_SIGN: String = "Ed25519" // 签名算法

    fun generateKeyPairEdDSA(): KeyPair {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_EDDSA)
            return keyPairGenerator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun sign(data: ByteArray?, privateKey: PrivateKey?): ByteArray {
        try {
            val signature: Signature = Signature.getInstance(ALGORITHM_SIGN)
            signature.initSign(privateKey)
            signature.update(data)
            return signature.sign()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: SignatureException) {
            throw RuntimeException(e)
        }
    }

    fun verify(data: ByteArray?, sign: ByteArray?, publicKey: PublicKey?): Boolean {
        try {
            val signature: Signature = Signature.getInstance(ALGORITHM_SIGN)
            signature.initVerify(publicKey)
            signature.update(data)
            return signature.verify(sign)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: SignatureException) {
            throw RuntimeException(e)
        }
    }

}