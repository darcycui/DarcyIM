package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.User
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {

    // Mysql默认大小写不敏感 这里使用BINARY 精确检查大小写
//    @Query("select u from User u where function('BINARY', u.username) = :userName")
    @Query("select u from User u where  u.username = :userName")
    fun findByUserName(userName: String): User?

    fun findByPhone(phone: String): User?

    fun findByEmail(email: String): User?

    fun findByStatusAndOnlineStatus(status: User.UserStatus, onlineStatus: User.OnlineStatus): List<User>

    @Query(
        "SELECT u FROM User u WHERE " +
                "(u.username LIKE %:keyword% OR u.nickname LIKE %:keyword% OR u.phone LIKE %:keyword%) " +
                "AND u.status = com.darcy.kotlin.server.demowebsocket.domain.table.User.UserStatus.NORMAL"
    )
    fun searchUsers(@Param("keyword") keyword: String, pageable: Pageable): Page<User>

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.onlineStatus = :status, u.lastActiveTime = :time WHERE u.id = :userId")
    fun updateOnlineStatus(
        @Param("userId") userId: Long,
        @Param("status") status: User.OnlineStatus,
        @Param("time") time: LocalDateTime
    ): Int

    @Query("SELECT u.onlineStatus FROM User u WHERE u.id = :userId")
    fun findOnlineStatusById(@Param("userId") userId: Long): User.OnlineStatus?

    fun existsByUsername(username: String): Boolean

    fun existsByPhone(phone: String): Boolean

    fun existsByEmail(email: String): Boolean
}