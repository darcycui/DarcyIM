package com.darcy.kotlin.server.demowebsocket.domain.table.media

import com.darcy.kotlin.server.demowebsocket.domain.table.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "UploadFile")
open class UploadFile(
    open var userId: Long = 0L,
    open var name: String = "",
    open var path: String = "",
    open var size: Long = 0L,
    open var type: FileType = FileType.OTHER,
    open var hash: String = ""
) : BaseEntity() {

    enum class FileType(private val code: Int) {
        IMAGE(1),
        VIDEO(2),
        AUDIO(3),
        DOCUMENT(4),
        OTHER(5),
        ;

        companion object {

            fun fromCode(code: Int): FileType {
                return entries.find { it.code == code } ?: OTHER
            }
        }

        fun toCode(): Int {
            return code
        }
    }
}

fun String.toFileType(): UploadFile.FileType {
    // 根据文件的contentType 返回对应的 FileType
    return when {
        this.startsWith("image/") -> UploadFile.FileType.IMAGE
        this.startsWith("video/") -> UploadFile.FileType.VIDEO
        this.startsWith("audio/") -> UploadFile.FileType.AUDIO
        this.startsWith("application/pdf") -> UploadFile.FileType.DOCUMENT
        this.startsWith("application/msword") -> UploadFile.FileType.DOCUMENT
        this.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") -> UploadFile.FileType.DOCUMENT
        this.startsWith("application/vnd.ms-excel") -> UploadFile.FileType.DOCUMENT
        this.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") -> UploadFile.FileType.DOCUMENT
        this.startsWith("application/vnd.ms-powerpoint") -> UploadFile.FileType.DOCUMENT
        this.startsWith("application/vnd.openxmlformats-officedocument.presentationml.presentation") -> UploadFile.FileType.DOCUMENT
        this.startsWith("text/") -> UploadFile.FileType.DOCUMENT
        else -> UploadFile.FileType.OTHER
    }
}