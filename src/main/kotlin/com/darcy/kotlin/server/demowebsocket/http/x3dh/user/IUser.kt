package com.darcy.kotlin.server.demowebsocket.http.x3dh.user

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey


interface IUser {
    fun getName(): String

    fun getIdentityKeyPair(): KeyPair

    fun getIdentityPublicKey(): PublicKey

    fun getIdentityPrivateKey(): PrivateKey

    fun getSignedPreKeyPair(): KeyPair

    fun getSignedPreKeyPublicKey(): PublicKey

    fun getSignedPreKeyPrivateKey(): PrivateKey

    fun getOneTimePreKeyPair(id: String): KeyPair

    fun getOneTimePreKeyPublicKeyList(): List<PublicKey>

    fun getOneTimePreKeyPrivateKeyList(): List<PrivateKey>
}