package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.dto.x3dh.X3DHBobKeysDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class X3DHService @Autowired constructor(
    private val identityKeyService: IdentityKeyService,
    private val signedPreKeyService: SignedPreKeyService,
    private val oneTimePreKeyService: OneTimePreKeyService
) {
    companion object {
        const val KEY_IDENTITY_KEY = "identityKey"
        const val KEY_SIGNED_PRE_KEY = "signedPreKey"
        const val KEY_ONE_TIME_PRE_KEY = "oneTimePreKey"
        const val KEY_EPHEMERAL_KEY = "ephemeralKey"
    }

    fun getBobPublicKeys(bobUserId: Long): X3DHBobKeysDTO {
        val identityKey = identityKeyService.queryByUserId(bobUserId)
        val signedPreKey = signedPreKeyService.queryByUserId(bobUserId)
        val oneTimePreKey = oneTimePreKeyService.queryFirstEnabled(bobUserId)
        return X3DHBobKeysDTO(
            identityKey.publicKey,
            signedPreKey.publicKey,
            oneTimePreKey.publicKey
        )
    }

    fun getAlicePublicKeys(aliceUserId: Long): Map<String, String> {
        return mapOf(
            KEY_IDENTITY_KEY to "",
            KEY_EPHEMERAL_KEY to "",
        )
    }
}