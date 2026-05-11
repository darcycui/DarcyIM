package com.darcy.kotlin.server.demowebsocket.http.x3dh.sign

import java.security.*
import java.security.spec.ECGenParameterSpec

object ECDSASignHelper {
    private const val ALGORITHM_ECDSA: String = "EC" // 椭圆曲线算法
    private const val CURVE_NAME: String = "secp256r1" // 指定椭圆曲线
    private const val ALGORITHM_SIGN: String = "SHA256withECDSA" // 签名算法

    fun generateKeyPairECDSA(): KeyPair {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_ECDSA)
            val ecSpec = ECGenParameterSpec(CURVE_NAME)
            keyPairGenerator.initialize(ecSpec, SecureRandom())
            return keyPairGenerator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
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