package com.darcy.kotlin.server.demowebsocket.http.repository

import com.darcy.kotlin.server.demowebsocket.domain.table.media.UploadFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UploadFileRepository : JpaRepository<UploadFile, Long> {
    @Query("select u from UploadFile u where u.hash = :hash")
    fun findByHash(hash: String) : List<UploadFile>
}