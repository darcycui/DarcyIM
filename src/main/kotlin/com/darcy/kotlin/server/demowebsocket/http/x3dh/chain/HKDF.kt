package com.darcy.kotlin.server.demowebsocket.http.x3dh.chain

import java.io.ByteArrayOutputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.ceil
import kotlin.math.min

object HKDF {
    private const val HASH_OUTPUT_SIZE: Int = 32

    fun deriveSecrets(inputKeyMaterial: ByteArray, salt: ByteArray, info: ByteArray?, outputLength: Int): ByteArray {
        val prk = extract(salt, inputKeyMaterial)
        return expand(prk, info, outputLength)
    }

    /**
     * 导出密钥
     * @param salt 盐
     * @param inputKeyMaterial 输入密钥
     * @return 密钥
     */
    private fun extract(salt: ByteArray, inputKeyMaterial: ByteArray): ByteArray {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(salt, "HmacSHA256"))
            return mac.doFinal(inputKeyMaterial)
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: InvalidKeyException) {
            throw AssertionError(e)
        }
    }

    /**
     * 拓展密钥到指定长度
     * @param prk 密钥
     * @param info 附加信息
     * @param outputSize 指定长度(字节)
     * @return 密钥
     */
    private fun expand(prk: ByteArray, info: ByteArray?, outputSize: Int): ByteArray {
        try {
            val iterations = ceil(outputSize.toDouble() / HASH_OUTPUT_SIZE.toDouble()).toInt()
            var mixin: ByteArray? = ByteArray(0)
            val results: ByteArrayOutputStream = ByteArrayOutputStream()
            var remainingBytes = outputSize
            val offset = getIterationStartOffset()
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(prk, "HmacSHA256"))
            for (i in offset until iterations + offset) {
                mac.reset()
                mac.update(mixin)
                info?.let { mac.update(it) }
                mac.update(i.toByte())
                val stepResult = mac.doFinal()
                val stepSize = min(remainingBytes.toDouble(), stepResult.size.toDouble()).toInt()
                results.write(stepResult, 0, stepSize)
                mixin = stepResult
                remainingBytes -= stepSize
            }
            return results.toByteArray()
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: InvalidKeyException) {
            throw AssertionError(e)
        }
    }

    private fun getIterationStartOffset(): Int {
        return 1
    }

}