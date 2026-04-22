package com.darcy.kotlin.server.demowebsocket.domain.dto.media

import com.darcy.kotlin.server.demowebsocket.domain.table.media.UploadFile
import com.darcy.kotlin.server.demowebsocket.utils.TimeUtil

data class UploadFileDTO(
    val userId: Long = 0L,
    val name: String = "",
    val path: String = "",
    val size: Long = 0L,
    val type: Int = 5,
    val hash: String = "",
    val id: Long = 0,
    val createdAt: String = "",
    val updatedAt: String = ""
)

fun UploadFile.toDTO(): UploadFileDTO {
    return UploadFileDTO(
        userId = this.userId,
        name = this.name,
        path = this.path,
        size = this.size,
        type = this.type.toCode(),
        hash = this.hash,
        id = this.id,
        createdAt = TimeUtil.formatDateTimeToString(this.createdAt),
        updatedAt = TimeUtil.formatDateTimeToString(this.updatedAt)
    )
}

fun List<UploadFile>.toDTO(): List<UploadFileDTO> {
    return this.map { it.toDTO() }
}

fun UploadFileDTO.toEntity(): UploadFile {
    return UploadFile(
        userId = this.userId,
        name = this.name,
        path = this.path,
        size = this.size,
        type = UploadFile.FileType.fromCode(this.type),
        hash = this.hash
    ).apply {
        this.id = this@toEntity.id
    }
}
