package com.darcy.kotlin.server.demowebsocket.http.x3dh.chain

import com.darcy.kotlin.server.demowebsocket.utils.EncryptUtil
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class ChainKey(
    private var kdf: HKDF,
    private var key: ByteArray,
    private var index: Int,
) {
    companion object {
        private val MESSAGE_KEY_SEED: ByteArray = byteArrayOf(0x01)
        private val CHAIN_KEY_SEED: ByteArray = byteArrayOf(0x02)
    }

    fun getKey(): ByteArray {
        return key
    }

    fun getIndex(): Int {
        return index
    }

    fun getNextChainKey(): ChainKey {
        val nextKey = getBaseMaterial(CHAIN_KEY_SEED)
        return ChainKey(kdf, nextKey, index + 1)
    }

    fun getMessageKeys(): ByteArray {
        val inputKeyMaterial = getBaseMaterial(MESSAGE_KEY_SEED)
        val keyMaterialBytes: ByteArray =
            kdf.deriveSecrets(inputKeyMaterial, ByteArray(32), "WhisperMessageKeys".toByteArray(), 80)
        val triple: Triple<ByteArray, ByteArray, ByteArray> = EncryptUtil.splitArray80(keyMaterialBytes, 32, 32, 16)
        val messageKey: ByteArray = triple.first
        val macKey: ByteArray = triple.second
        val iv: ByteArray = triple.third
        return messageKey
    }

    private fun getBaseMaterial(seed: ByteArray): ByteArray {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(key, "HmacSHA256"))

            return mac.doFinal(seed)
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: InvalidKeyException) {
            throw AssertionError(e)
        }
    }

}