package com.darcy.kotlin.server.demowebsocket.http.x3dh.user

import com.darcy.kotlin.server.demowebsocket.http.x3dh.exchange.ECCExchangeHelper
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

abstract class BaseUser() : IUser {
    protected var innerIdentityKeyPair: KeyPair = ECCExchangeHelper.generateKeyPair()
    protected var innerSignedPreKeyPair: KeyPair = ECCExchangeHelper.generateKeyPair()
    protected var innerOneTimePreKeyPairMap: MutableMap<String, KeyPair> = HashMap(100)

    init {
        for (i in 0..99) {
            val item: KeyPair = ECCExchangeHelper.generateKeyPair()
            innerOneTimePreKeyPairMap[(i + 1).toString()] = item
        }
    }

    override fun getIdentityKeyPair(): KeyPair {
        return innerIdentityKeyPair
    }

    override fun getIdentityPublicKey(): PublicKey {
        return innerIdentityKeyPair.public
    }

    override fun getIdentityPrivateKey(): PrivateKey {
        return innerIdentityKeyPair.private
    }

    override fun getSignedPreKeyPair(): KeyPair {
        return innerSignedPreKeyPair
    }

    override fun getSignedPreKeyPublicKey(): PublicKey {
        return innerSignedPreKeyPair.public
    }

    override fun getSignedPreKeyPrivateKey(): PrivateKey {
        return innerSignedPreKeyPair.private
    }

    override fun getOneTimePreKeyPair(id: String): KeyPair {
        if (!innerOneTimePreKeyPairMap.containsKey(id)) {
            throw RuntimeException("OneTimePreKeyPair 不存在:$id")
        }
        return innerOneTimePreKeyPairMap[id]!!
    }

    override fun getOneTimePreKeyPublicKeyList(): List<PublicKey> {
        return innerOneTimePreKeyPairMap.values.toList().map { it.public }
    }

    override fun getOneTimePreKeyPrivateKeyList(): List<PrivateKey> {
        return innerOneTimePreKeyPairMap.values.toList().map { it.private }
    }
}