package com.darcy.kotlin.server.demowebsocket.x3dh

import com.darcy.kotlin.server.demowebsocket.http.x3dh.encrypt.keyToString
import com.darcy.kotlin.server.demowebsocket.http.x3dh.exchange.ECCExchangeHelper
import com.darcy.kotlin.server.demowebsocket.http.x3dh.user.Alice
import org.junit.jupiter.api.Test
import java.security.KeyPair

class KeyGenerateTests {

    /**
     * 注册时创建密钥对
     *
     * Alice
     * identityPrivateKey: 302e020100300506032b656e042204208c4aae7a93367905f9f8a68491173059bfd53aa6ccb9906ba59d247f650b1231
     * identityPublicKey: 302a300506032b656e0321003d4fa7151d41dd6242145c651e3b26f2c8c2b285e28f6843bffd82d6232d832a
     * signedPreKeyPrivateKey: 302e020100300506032b656e0422042093a94d5628c2cd646a3deffe5d34e129901e98e48c2de71b39c7774bcb4e7129
     * signedPreKeyPublicKey: 302a300506032b656e0321005bc3e621c2258b07efd86ec9873b520757ddd7ae918ed984ee92c8044997eb4f
     * oneTimePreKeyPrivateKeyList: [
     * 302e020100300506032b656e042204202cc702c84cc7e7b60aa27a686b30c8776a4c6efabb425267b8f5739664a05a8e,
     * 302e020100300506032b656e0422042022733d32ee1d1119d0083ed91560c1b776b6aae8ea2fd9ce5d294384abe66501,
     * 302e020100300506032b656e0422042050bda5f86e89a0c7c3bdb9466c3c5e439e279b9014711b01665a510a87c63fcf]
     * oneTimePreKeyPublicKeyList: [302a300506032b656e0321000034e801787ce9892a7481d71c9cb62e3214f34314a7ef4e18615b1fb0338564,
     * 302a300506032b656e0321007160fc371e20560cd0fb0b95b15219c514a9e235db638c8f31f9aa2ad99a0908,
     * 302a300506032b656e032100235e722d08a46c187b8d70c0ecfcaa157d03c6ca8209ecc9d3592c333b94a06e]
     *
     * Bob
     * identityPrivateKey: 302e020100300506032b656e04220420286e814b3eb5bcee87f41fa35e68b73d24be78b46adb789c65dd0f917cb8239c
     * identityPublicKey: 302a300506032b656e03210045719fdb359ba56c3115dcde90533459559cb79abe918ff8c25710fa19551d6d
     * signedPreKeyPrivateKey: 302e020100300506032b656e0422042051bbbd6b05dc99556ed59688154f8895d44d70897fa567e933d121d33b7f9a96
     * signedPreKeyPublicKey: 302a300506032b656e0321002fc06c206dee050b7f0f7869dddeced0f9cccc0be79b015bb2f0bb01bf24b666
     * oneTimePreKeyPrivateKeyList: [302e020100300506032b656e0422042055f2e93e1f74ce86dfbf8772264a216c6cb3e69979fd50771fb5c7c7841f53a0,
     * 302e020100300506032b656e042204205cbf410b841f22b34adacde5341194ec5e03cad4e6a54fd16c70c857cee2dc68,
     * 302e020100300506032b656e04220420626a6db6fd82a82982867703ec224487eb62ab846ebe5ac8e4485851d65904ea]
     * oneTimePreKeyPublicKeyList: [302a300506032b656e032100831d0ea4848fc45db766e68a0014230cd967fddf603e37fdea5422ca1737062e,
     * 302a300506032b656e0321005569be96b36c955310a9f2696ae2228fef164128d1159a72393888474985743f,
     * 302a300506032b656e0321000ca598f585b5f9cf3109a523b3b10a54aa75258191316f5a4e22235a7ec2321a]
     */
    @Test
    fun `test-create-register-keys`(){
        val alice = Alice()
        println("identityPrivateKey: ${alice.getIdentityPrivateKey().keyToString()}")
        println("identityPublicKey: ${alice.getIdentityPublicKey().keyToString()}")
        println("signedPreKeyPrivateKey: ${alice.getSignedPreKeyPrivateKey().keyToString()}")
        println("signedPreKeyPublicKey: ${alice.getSignedPreKeyPublicKey().keyToString()}")
        println("oneTimePreKeyPrivateKeyList: ${alice.getOneTimePreKeyPrivateKeyList().map { it.keyToString() }}")
        println("oneTimePreKeyPublicKeyList: ${alice.getOneTimePreKeyPublicKeyList().map { it.keyToString() }}")
    }

    /**
     * 测试创建 DH 密钥对
     * ephemeralPrivateKey: 302e020100300506032b656e04220420f1b760d87917b117017d2328792fb28b95e652bd71d7c4db44c18b1e3dc79337
     * ephemeralPublicKey: 302a300506032b656e032100a0b9b7d397986c7c2c492d6b03039eeccba10c32ad86538546a46fb2b0b07226
     */
    @Test
    fun `test-create-dh-keys`(){
        val ephemeralKeyPair: KeyPair = ECCExchangeHelper.generateKeyPair()
        println("ephemeralPrivateKey: ${ephemeralKeyPair.private.keyToString()} ")
        println("ephemeralPublicKey: ${ephemeralKeyPair.public.keyToString()}")
    }
}