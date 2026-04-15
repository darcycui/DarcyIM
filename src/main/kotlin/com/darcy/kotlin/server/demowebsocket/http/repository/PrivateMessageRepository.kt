package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.message.PrivateMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
interface PrivateMessageRepository : JpaRepository<PrivateMessage, Long> {

    fun findByMsgId(msgId: String): Optional<PrivateMessage>

    @Query(
        "SELECT pm FROM PrivateMessage pm WHERE " +
                "((pm.sender.id = :userId1 AND pm.receiver.id = :userId2) OR " +
                "(pm.sender.id = :userId2 AND pm.receiver.id = :userId1)) " +
                "AND pm.sendTime < :beforeTime " +
                "AND (pm.isDeletedBySender = false OR pm.sender.id != :userId1) " +
                "AND (pm.isDeletedByReceiver = false OR pm.receiver.id != :userId2) " +
                "ORDER BY pm.sendTime DESC"
    )
    fun findHistoryMessages(
        @Param("userId1") userId1: Long,
        @Param("userId2") userId2: Long,
        @Param("beforeTime") beforeTime: LocalDateTime,
        pageable: Pageable
    ): Page<PrivateMessage>

    @Query(
        "SELECT pm FROM PrivateMessage pm WHERE " +
                "pm.sender.id = :senderId AND pm.receiver.id = :receiverId " +
                "AND pm.sendTime > :afterTime " +
                "AND pm.isRecalled = false " +
                "ORDER BY pm.sendTime ASC"
    )
    fun findNewMessages(
        @Param("senderId") senderId: Long,
        @Param("receiverId") receiverId: Long,
        @Param("afterTime") afterTime: LocalDateTime
    ): List<PrivateMessage>

    @Modifying
    @Transactional
    @Query(
        "UPDATE PrivateMessage pm SET pm.isRead = true, pm.readTime = :readTime " +
                "WHERE pm.msgId IN :msgIds AND pm.receiver.id = :userId"
    )
    fun markMessagesAsRead(
        @Param("userId") userId: Long,
        @Param("msgIds") msgIds: List<String>,
        @Param("readTime") readTime: LocalDateTime
    ): Int

    @Modifying
    @Transactional
    @Query(
        "UPDATE PrivateMessage pm SET pm.isRecalled = true, pm.recallTime = :recallTime " +
                "WHERE pm.msgId = :msgId AND pm.sender.id = :userId"
    )
    fun recallMessage(
        @Param("userId") userId: Long,
        @Param("msgId") msgId: String,
        @Param("recallTime") recallTime: LocalDateTime
    ): Int

    @Query(
        "SELECT COUNT(pm) FROM PrivateMessage pm WHERE " +
                "pm.receiver.id = :userId AND pm.isRead = false AND pm.isRecalled = false " +
                "AND (pm.isDeletedByReceiver = false)"
    )
    fun countUnreadMessages(@Param("userId") userId: Long): Long

    fun findBySenderIdAndReceiverIdAndIsReadFalse(
        senderId: Long,
        receiverId: Long
    ): List<PrivateMessage>

    @Query(
        "SELECT pm FROM PrivateMessage pm WHERE " +
                "pm.sender.id = :userId AND pm.sendTime BETWEEN :startTime AND :endTime"
    )
    fun findMessagesByTimeRange(
        @Param("userId") userId: Long,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<PrivateMessage>
}