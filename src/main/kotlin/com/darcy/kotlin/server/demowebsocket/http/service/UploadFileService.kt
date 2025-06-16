package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.UploadFileEntity
import com.darcy.kotlin.server.demowebsocket.http.repository.ImageFileRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.UploadFileDatabaseRepository
import com.darcy.kotlin.server.demowebsocket.log.LOGGER
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class UploadFileService @Autowired constructor(
    val imageFileRepository: ImageFileRepository,
    val uploadFileDatabaseRepository: UploadFileDatabaseRepository
) {
    // @Value注解注入了文件保存路径uploadPath 配置在application.properties或application.yml文件中
    @Value("\${upload.path.image}")
    private val imageDir: String = ""

    fun saveImageFile(file: MultipartFile): File? {
        return imageFileRepository.saveImageFile(file, imageDir)
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

    fun updateItem(
        id: Long,
        userId: Long,
        name: String,
        path: String,
        size: Long,
        type: String,
        hash: String
    ): UploadFileEntity? {
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

    fun getFileResourceByName(fileName: String): Triple<String, String, Resource?> {
        val fileStoragePath: Path = Paths.get(imageDir)
        // 构造文件路径 normalize() 防止遍历攻击
        val filePath = fileStoragePath.resolve(fileName).normalize()
        val contentType: String = Files.probeContentType(filePath) ?: "application/octet-stream"
        val resource = UrlResource(filePath.toUri())
        val contentDisposition = "attachment; filename=\"${resource.filename}\""

        // 验证文件是否存在且可读
        if (!resource.exists() || !resource.isReadable) {
            return Triple(contentType, contentDisposition, null)
        }
        return Triple(contentType, contentDisposition, resource)
    }
}