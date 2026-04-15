package com.darcy.kotlin.server.demowebsocket.domain.table

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = -1L

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    open lateinit var createdAt: LocalDateTime

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    open lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as BaseEntity
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}(id=$id, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

}