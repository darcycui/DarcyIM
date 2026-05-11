package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.x3dh.IdentityKey
import com.darcy.kotlin.server.demowebsocket.http.repository.IdentityKeyRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.OneTimePreKeyRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.SignedPreKeyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IdentityKeyService @Autowired constructor(
    private val identityKeyRepository: IdentityKeyRepository,
    private val signedPreKeyRepository: SignedPreKeyRepository,
    private val oneTimePreKeyRepository: OneTimePreKeyRepository,
    private val userService: UserService,
    private val deviceService: DeviceService
) {
    @Transactional
    fun createIdentityKey(userId: Long, publicKey: String): IdentityKey {
        // 使用device 之前要先保存到数据库

        val identityKey = IdentityKey(
            user = userService.queryUserById(userId),
            publicKey = publicKey,
        )
        return identityKeyRepository.save(identityKey)
    }

    fun queryByUserId(userId: Long): IdentityKey? {
        return identityKeyRepository.findByUserId(userId)
    }
}