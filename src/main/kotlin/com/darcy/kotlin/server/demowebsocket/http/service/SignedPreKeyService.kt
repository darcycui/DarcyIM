package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.SignedPreKey
import com.darcy.kotlin.server.demowebsocket.exception.code1000.X3DHException
import com.darcy.kotlin.server.demowebsocket.http.repository.DeviceRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.IdentityKeyRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.OneTimePreKeyRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.SignedPreKeyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SignedPreKeyService @Autowired constructor(
    private val identityKeyRepository: IdentityKeyRepository,
    private val signedPreKeyRepository: SignedPreKeyRepository,
    private val oneTimePreKeyRepository: OneTimePreKeyRepository,
    private val userService: UserService,
    private val deviceRepository: DeviceRepository
) {
    fun createSignedPreKey(userId: Long, publicKey: String): SignedPreKey {
        val user = userService.queryUserById(userId)
        val signedPreKey = SignedPreKey(
            user = user,
            publicKey = publicKey,
        )
        return signedPreKeyRepository.save(signedPreKey)
    }

    fun queryByUserId(userId: Long): SignedPreKey {
        return signedPreKeyRepository.findByUserId(userId) ?: throw X3DHException.SIGNED_PRE_KEY_NOT_EXIST
    }
}