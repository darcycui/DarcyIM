package com.darcy.kotlin.server.demowebsocket.domain.table.message

import com.darcy.kotlin.server.demowebsocket.domain.table.BaseEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

/**
 * 使用JPA注解创建数据库表
 */
@Entity
@Table(
    name = "private_messages",
    indexes = [
        Index(name = "idx_sender_receiver", columnList = "sender_id, receiver_id, send_time"),
        Index(name = "idx_receiver_sender", columnList = "receiver_id, sender_id, send_time"),
        Index(name = "idx_msg_id", columnList = "msg_id", unique = true),
        Index(name = "idx_send_time", columnList = "send_time"),
        Index(name = "idx_is_read", columnList = "is_read, receiver_id")
    ]
)
@DynamicInsert
open class PrivateMessage(

    @Column(name = "msg_id", nullable = false, length = 64, unique = true)
    open var msgId: String = "",

    // 多对一 多个私聊消息对应一个发送者
    // 设置sender_id是外键 关联到用户表 外键名为fk_private_message_sender
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false, foreignKey = ForeignKey(name = "fk_private_message_sender"))
    open var sender: User,

    // 多对一 多个私聊消息对应一个接收者
    // 设置receiver_id是外键 关联到用户表 外键名为fk_private_message_receiver
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false, foreignKey = ForeignKey(name = "fk_private_message_receiver"))
    open var receiver: User,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "msg_type", nullable = false)
    open var msgType: MessageType = MessageType.TEXT,

    @Lob
    @Column(name = "content")
    open var content: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extra_data", columnDefinition = "json")
    open var extraData: Map<String, Any> = emptyMap(),

    @Column(name = "is_read", nullable = false)
    open var isRead: Boolean = false,

    @Column(name = "is_recalled", nullable = false)
    open var isRecalled: Boolean = false,

    @Column(name = "is_deleted_by_sender", nullable = false)
    open var isDeletedBySender: Boolean = false,

    @Column(name = "is_deleted_by_receiver", nullable = false)
    open var isDeletedByReceiver: Boolean = false,

    @Column(name = "seq_id", nullable = false)
    open var seqId: Long = 0L,

    @Column(name = "send_time", nullable = false)
    open var sendTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "read_time")
    open var readTime: LocalDateTime? = null,

    @Column(name = "recall_time")
    open var recallTime: LocalDateTime? = null,

    @Column(name = "reply_to_msg_id", length = 64)
    open var replyToMsgId: String? = null,

    @Column(name = "client_msg_id", length = 64)
    open var clientMsgId: String? = null,

    @Column(name = "client_type", length = 20)
    open var clientType: String? = null,

    @Column(name = "client_version", length = 20)
    open var clientVersion: String? = null
) : BaseEntity() {

    enum class MessageType {
        TEXT,           // 文本
        IMAGE,          // 图片
        VOICE,          // 语音
        VIDEO,          // 视频
        FILE,           // 文件
        LOCATION,       // 位置
        CARD,           // 名片
        EMOJI,          // 表情
        SYSTEM,         // 系统消息
        CUSTOM          // 自定义消息
    }

    fun markAsRead(time: LocalDateTime = LocalDateTime.now()) {
        isRead = true
        readTime = time
    }

    fun recall(time: LocalDateTime = LocalDateTime.now()) {
        isRecalled = true
        recallTime = time
    }

    fun canRecall(recallWindowMinutes: Long = 2): Boolean {
        val now = LocalDateTime.now()
        val recallDeadline = sendTime.plusMinutes(recallWindowMinutes)
        return now.isBefore(recallDeadline) && !isRecalled
    }
}