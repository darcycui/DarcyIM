package com.darcy.kotlin.server.demowebsocket.domain.table

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@Table(
    name = "friend_requests",
    indexes = [
        Index(name = "idx_from_user", columnList = "from_user_id, status, created_at"),
        Index(name = "idx_to_user", columnList = "to_user_id, status, created_at")
    ]
)
@DynamicInsert
@DynamicUpdate
open class FriendRequest(
    // 多对一 多个好友请求对应一个用户
    // 设置外键 from_user_id 关联到用户表 外键名为fk_friend_request_from_user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false, foreignKey = ForeignKey(name = "fk_friend_request_from_user"))
    open var fromUser: User,

    // 多对一 多个好友请求对应一个用户
    // 设置外键 to_user_id 关联到用户表 外键名为fk_friend_request_to_user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false, foreignKey = ForeignKey(name = "fk_friend_request_to_user"))
    open var toUser: User,

    @Column(name = "greeting", length = 200)
    open var greeting: String? = null,

    @Column(name = "remark", length = 200)
    open var remark: String? = null,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    open var status: RequestStatus = RequestStatus.PENDING,

    @Column(name = "handle_time")
    open var handleTime: LocalDateTime? = null,

    @Column(name = "handle_result", length = 50)
    open var handleResult: String? = null
) : BaseEntity() {

    enum class RequestStatus {
        PENDING,    // 0-待处理
        ACCEPTED,   // 1-已接受
        REJECTED,   // 2-已拒绝
        IGNORED,    // 3-已忽略
        EXPIRED     // 4-已过期
    }

    fun accept(remark: String? = null) {
        status = RequestStatus.ACCEPTED
        handleTime = LocalDateTime.now()
        handleResult = remark
    }

    fun reject(remark: String? = null) {
        status = RequestStatus.REJECTED
        handleTime = LocalDateTime.now()
        handleResult = remark
    }

    fun isProcessed(): Boolean = status != RequestStatus.PENDING
}