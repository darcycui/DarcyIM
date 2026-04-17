package com.darcy.kotlin.server.demowebsocket.domain.table

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

/**
 * 使用JPA注解创建数据库表
 */
@Entity()
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_username", columnList = "username", unique = true),
        Index(name = "idx_phone", columnList = "phone", unique = true),
        Index(name = "idx_email", columnList = "email", unique = true),
        Index(name = "idx_online_status", columnList = "online_status, last_active_time"),
    ]
)
@DynamicInsert
@DynamicUpdate
open class User(
    @Column(name = "username", nullable = false, length = 64, unique = true)
    open var username: String = "",
    @Column(name = "password_hash", nullable = false, length = 255)
    open var passwordHash: String = "",
    @Column(name = "nickname", nullable = false, length = 64)
    open var nickname: String = "",

    @Column(name = "avatar", length = 512)
    open var avatar: String = "",

    @Column(name = "phone", length = 20, unique = true)
    open var phone: String = "",

    @Column(name = "email", length = 100, unique = true)
    open var email: String = "",

    @Column(name = "gender", length = 10)
    open var gender: String = "",

    @Column(name = "signature", length = 200)
    open var signature: String = "",

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    open var status: UserStatus = UserStatus.NORMAL,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "online_status", nullable = false)
    open var onlineStatus: OnlineStatus = OnlineStatus.OFFLINE,

    @Column(name = "last_active_time")
    open var lastActiveTime: LocalDateTime? = null,

    @Column(name = "deleted_at")
    open var deletedAt: LocalDateTime? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settings", columnDefinition = "json")
    open var settings: Map<String, Any> = emptyMap(),

    open var roles: String = "",
    @Transient
    open var token: String = ""
) : BaseEntity() {
    companion object {
        val EMPTY = User()
    }

    enum class UserStatus {
        DISABLED,   // 0-禁用
        NORMAL      // 1-正常
    }

    enum class OnlineStatus {
        OFFLINE,    // 0-离线
        ONLINE,     // 1-在线
        BUSY,       // 2-忙碌
        STEALTH     // 3-隐身
    }

    fun isOnline(): Boolean = onlineStatus == OnlineStatus.ONLINE

    fun updateLastActiveTime() {
        lastActiveTime = LocalDateTime.now()
    }
}