package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.UploadFileEntity
import com.darcy.kotlin.server.demowebsocket.http.repository.ImageFileRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.UploadFileDatabaseRepository
import com.darcy.kotlin.server.demowebsocket.log.LOGGER
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class UploadFileService @Autowired constructor(
    val imageFileRepository: ImageFileRepository,
    val uploadFileDatabaseRepository: UploadFileDatabaseRepository
) {
    fun saveFile(file: MultipartFile): File? {
        return imageFileRepository.saveFileToStorage(file)
    }

    fun calculateFileHash(file: File): String {
        return imageFileRepository.calculateFileHash(file, "SHA-1")
    }

    @Transactional
    fun createItem(userId: Long, name: String, path: String, size: Long, type: String, hash: String): UploadFileEntity {
        val existItem = uploadFileDatabaseRepository.findByHash(hash).firstOrNull()
        if (existItem != null) {
            LOGGER.warn("数据库已存在:$name 无需写入数据库")
            return existItem
        }
        LOGGER.info("数据库不存在:$name 写入数据库.")
        return uploadFileDatabaseRepository.save(
            UploadFileEntity(
                userId = userId, name = name, path = path, size = size, type = type, hash = hash
            )
        )
    }

    fun getItemId(id: Long): UploadFileEntity? {
        return uploadFileDatabaseRepository.findById(id).orElse(null)
    }

    fun updateItem(id: Long, userId: Long, name: String, path: String, size: Long, type: String, hash: String): UploadFileEntity? {
        val file = getItemId(id)
        if (file != null) {
            file.userId = userId
            file.name = name
            file.path = path
            file.size = size
            file.type = type
            file.hash = hash
            return uploadFileDatabaseRepository.save(file)
        }
        return null
    }

    fun deleteItem(id: Long): Boolean {
        val file = getItemId(id)
        if (file != null) {
            uploadFileDatabaseRepository.delete(file)
            return true
        }
        return false
    }
}