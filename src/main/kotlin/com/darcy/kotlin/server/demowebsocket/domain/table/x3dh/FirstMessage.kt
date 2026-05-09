package com.darcy.kotlin.server.demowebsocket.domain.table.x3dh

import com.darcy.kotlin.server.demowebsocket.domain.table.BaseEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

/**
 * 离线消息表 - X3DH密钥交换协议
 */
@Entity
@Table(
    name = "first_message",
    indexes = [
        Index(name = "idx_receiver_identity", columnList = "receiver_identity_key"),
    ],
    uniqueConstraints = [
        UniqueConstraint(name = "uk_message_id", columnNames = ["message_id"])
    ]
)
@DynamicInsert
@DynamicUpdate
open class FirstMessage(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from_id", nullable = false, foreignKey = ForeignKey(name = "fk_first_message_user_from"))
    open var userFrom: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to_id", nullable = false, foreignKey = ForeignKey(name = "fk_first_message_user_to"))
    open var userTo: User,

    @Column(name = "message_id", nullable = false, length = 128, unique = true)
    open var messageId: String = "",

    @Column(name = "sender_identity_key", nullable = false, length = 256)
    open var senderIdentityKey: String = "",

    @Column(name = "receiver_identity_key", nullable = false, length = 256)
    open var receiverIdentityKey: String = "",

    @Column(name = "ephemeral_public_key", nullable = false, columnDefinition = "TEXT")
    open var ephemeralPublicKey: String = "",

    @Column(name = "used_signed_pre_key_id", nullable = false)
    open var usedSignedPreKeyId: Int = 0,

    @Column(name = "used_one_time_pre_key_id")
    open var usedOneTimePreKeyId: Int? = null,

    @Column(name = "ciphertext", nullable = false, columnDefinition = "TEXT")
    open var ciphertext: String = "",
) : BaseEntity() {

    override fun toString(): String {
        return "OfflineMessage(id=$id, messageId=$messageId, sender=${senderIdentityKey.take(20)}..., " +
                "receiver=${receiverIdentityKey.take(20)}...)" +
                "ephemeralPublicKey=${ephemeralPublicKey.take(20)}..., " +
                "usedSignedPreKeyId=$usedSignedPreKeyId, usedOneTimePreKeyId=$usedOneTimePreKeyId)" +
                "ciphertext=${ciphertext.take(20)}...)"
    }
}
