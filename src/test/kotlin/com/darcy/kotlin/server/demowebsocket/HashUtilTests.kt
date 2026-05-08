package com.darcy.kotlin.server.demowebsocket

import com.darcy.kotlin.server.demowebsocket.utils.HashUtil
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HashUtilTests {
    @Test
    fun `test-sha256`() {
        val original = "hello world"
        val digest = HashUtil.sha256Str(original)
        println(digest)
        assertEquals(
            "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9",
            digest,
            "digest should be equal"
        )
    }
}