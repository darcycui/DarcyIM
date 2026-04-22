package com.darcy.kotlin.server.demowebsocket.domain.dto.string

import org.springframework.data.domain.Page

data class StringDTO(
    val content: String = ""
) {
}

fun String.toDTO(): StringDTO {
    return StringDTO(this)
}

fun List<String>.toDTO(): List<StringDTO> {
    return this.map { it.toDTO() }
}

fun Page<String>.toDTO(): Page<StringDTO> {
    return this.map { it.toDTO() }
}