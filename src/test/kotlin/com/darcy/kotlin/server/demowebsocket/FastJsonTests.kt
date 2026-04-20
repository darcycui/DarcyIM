package com.darcy.kotlin.server.demowebsocket

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONReader
import com.alibaba.fastjson2.TypeReference
import com.alibaba.fastjson2.annotation.JSONField
import com.darcy.kotlin.server.demowebsocket.domain.ResultEntity
import org.junit.jupiter.api.Test

class FastJsonTests {
    private val json = """
        {
          "resultcode": 200,
          "reason": "success",
          "error_code": 0,
          "result": {
              "content": [
                {
                  "sender": {"id": 1, "name": "Alice"},
                  "receiver2": {"id": 2, "name": "Bob"}
                }
              ]
          }
          
        }
    """.trimIndent()

    class User {
        @JSONField(name = "id")
        var id: Int = 0

        @JSONField(name = "name")
        var name: String = ""

        override fun toString(): String {
            return "User(id=$id, name='$name')"
        }
    }

    class Message {
        @JSONField(name = "sender")
        var sender: User? = null

        @JSONField(name = "receiver2")
        var receiver2: User? = null

        override fun toString(): String {
            return "Message(sender=$sender, receiver2=$receiver2)"
        }
    }

    class MessageResult {
        @JSONField(name = "content")
        var content: List<Message>? = null

        override fun toString(): String {
            return "MessageResult(content=$content)"
        }
    }

    @Test
    fun `test-fastjson-ref-field`() {
        val resultEntity = JSONObject.parseObject(
            json,
            object : TypeReference<ResultEntity<MessageResult>>() {},
//            JSONReader.Feature.DisableReferenceDetect
        )
        println("resultEntity--$resultEntity")
        println("resultEntity--${resultEntity.toJsonString()}")
    }
}