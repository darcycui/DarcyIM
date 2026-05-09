package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Device
import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.IdentityKey
import com.darcy.kotlin.server.demowebsocket.http.repository.IdentityKeyRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.OneTimePreKeyRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.SignedPreKeyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IdentityKeyService @Autowired constructor(
    private val identityKeyRepository: IdentityKeyRepository,
    private val signedPreKeyRepository: SignedPreKeyRepository,
    private val oneTimePreKeyRepository: OneTimePreKeyRepository,
    private val userService: UserService,
    private val deviceService: DeviceService
) {
    fun createIdentityKey(userId: Long, deviceName: String, publicKey: String): IdentityKey {
        val device = deviceService.queryByUserIdAndDeviceName(userId, deviceName) ?: Device(
            user = userService.queryUserById(userId),
            name = deviceName
        )
        val identityKey = IdentityKey(
            user = userService.queryUserById(userId),
            device = device,
            publicKey = publicKey,
        )
        return identityKeyRepository.save(identityKey)
    }

    fun queryByUserId(userId: Long): IdentityKey? {
        return identityKeyRepository.findByUserId(userId)
    }
}