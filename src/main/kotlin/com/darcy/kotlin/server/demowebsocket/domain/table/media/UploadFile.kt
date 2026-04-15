package com.darcy.kotlin.server.demowebsocket.domain.table.media

import com.darcy.kotlin.server.demowebsocket.domain.table.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "UploadFile")
open class UploadFile(
    open var userId: Long = -1L,
    open var name: String = "",
    open var path: String = "",
    open var size: Long = 0L,
    open var type: String = "",
    open var hash: String = ""
) : BaseEntity()