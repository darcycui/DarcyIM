package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.Conversation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
interface ConversationRepository : JpaRepository<Conversation, Long> {

    fun findByUserIdAndConversationTypeAndTargetId(
        userId: Long,
        conversationType: Conversation.ConversationType,
        targetId: Long
    ): Conversation?

    fun findByUserIdOrderByLastMsgTimeDesc(userId: Long): List<Conversation>

    fun findByUserIdAndIsPinnedTrueOrderByLastMsgTimeDesc(userId: Long): List<Conversation>

    fun findByUserIdAndIsMutedTrue(userId: Long): List<Conversation>

    fun findByUserId(userId: Long): List<Conversation>

    @Modifying
    @Transactional
    @Query(
        """
        UPDATE Conversation c SET 
        c.unreadCount = c.unreadCount + 1, 
        c.lastMsgId = :msgId, 
        c.lastMsgContent = :content, 
        c.lastMsgType = :msgType, 
        c.lastMsgSenderId = :senderId, 
        c.lastMsgTime = :msgTime 
        WHERE c.user.id = :userId AND c.conversationType = :conversationType 
        AND c.targetId = :targetId
    """
    )
    fun updateConversationOnNewMessage(
        @Param("userId") userId: Long,
        @Param("conversationType") conversationType: Conversation.ConversationType,
        @Param("targetId") targetId: Long,
        @Param("msgId") msgId: String,
        @Param("content") content: String,
        @Param("msgType") msgType: Int,
        @Param("senderId") senderId: Long,
        @Param("msgTime") msgTime: LocalDateTime
    ): Int

    @Modifying
    @Transactional
    @Query("UPDATE Conversation c SET c.unreadCount = 0 WHERE c.id = :conversationId")
    fun clearUnreadCount(@Param("conversationId") conversationId: Long): Int

    @Modifying
    @Transactional
    @Query("UPDATE Conversation c SET c.isMuted = :isMuted WHERE c.id = :conversationId")
    fun updateMuteStatus(
        @Param("conversationId") conversationId: Long,
        @Param("isMuted") isMuted: Boolean
    ): Int

    @Modifying
    @Transactional
    @Query("UPDATE Conversation c SET c.isPinned = :isPinned WHERE c.id = :conversationId")
    fun updatePinStatus(
        @Param("conversationId") conversationId: Long,
        @Param("isPinned") isPinned: Boolean
    ): Int

    @Modifying
    @Transactional
    @Query("DELETE FROM Conversation c WHERE c.user.id = :userId AND c.conversationType = :conversationType AND c.targetId = :targetId")
    fun deleteByUserAndTarget(
        @Param("userId") userId: Long,
        @Param("conversationType") conversationType: Conversation.ConversationType,
        @Param("targetId") targetId: Long
    ): Int

    fun countByUserIdAndUnreadCountGreaterThan(userId: Long, unreadCount: Int): Long
}