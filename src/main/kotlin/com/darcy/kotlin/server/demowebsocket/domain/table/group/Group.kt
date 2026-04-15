package com.darcy.kotlin.server.demowebsocket.domain.table.group

import com.darcy.kotlin.server.demowebsocket.domain.table.BaseEntity
import com.darcy.kotlin.server.demowebsocket.domain.table.User
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity
@Table(
    name = "`groups`",
    indexes = [
        Index(name = "idx_group_id", columnList = "group_id", unique = true),
        Index(name = "idx_owner", columnList = "owner_id"),
        Index(name = "idx_status", columnList = "status, created_at")
    ]
)
@DynamicInsert
@DynamicUpdate
open class Group(

    @Column(name = "group_id", nullable = false, length = 32, unique = true)
    open var groupId: String = "",

    @Column(name = "group_name", nullable = false, length = 100)
    open var groupName: String = "",

    @Column(name = "avatar", length = 512)
    open var avatar: String? = null,

    @Column(name = "description", length = 500)
    open var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    open var owner: User,

    @Column(name = "max_members", nullable = false)
    open var maxMembers: Int = 500,

    @Column(name = "current_members", nullable = false)
    open var currentMembers: Int = 1,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "join_type", nullable = false)
    open var joinType: JoinType = JoinType.VERIFICATION,

    @Column(name = "join_question", length = 200)
    open var joinQuestion: String? = null,

    @Column(name = "join_answer", length = 200)
    open var joinAnswer: String? = null,

    @Lob
    @Column(name = "announcement")
    open var announcement: String? = null,

    @Column(name = "announcement_publisher_id")
    open var announcementPublisherId: Long? = null,

    @Column(name = "announcement_publish_time")
    open var announcementPublishTime: LocalDateTime? = null,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    open var status: GroupStatus = GroupStatus.NORMAL,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "permissions", columnDefinition = "json")
    open var permissions: Map<String, Boolean> = emptyMap(),

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var members: MutableList<GroupMember> = mutableListOf()
) : BaseEntity() {


    enum class GroupStatus {
        DISSOLVED,  // 已解散
        NORMAL,     // 正常
        BANNED      // 被封禁
    }

    enum class JoinType {
        FREE,           // 自由加入
        VERIFICATION,   // 需要验证
        INVITATION      // 仅限邀请
    }

    fun isFull(): Boolean = currentMembers >= maxMembers

    fun canJoin(): Boolean = status == GroupStatus.NORMAL && !isFull()

    fun addMember(member: GroupMember) {
        if (isFull()) throw IllegalStateException("群组已满")
        members.add(member)
        member.group = this
        currentMembers++
    }

    fun removeMember(member: GroupMember) {
        members.remove(member)
        currentMembers--
    }
}