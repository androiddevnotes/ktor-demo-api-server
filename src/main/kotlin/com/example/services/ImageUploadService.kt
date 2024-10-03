package com.example.services

import java.io.File
import java.util.UUID
import io.ktor.http.content.*

class ImageUploadService(private val uploadDir: String) {
    init {
        File(uploadDir).mkdirs()
    }

    fun saveImage(part: PartData.FileItem): String {
        val originalFileName = part.originalFileName ?: "unnamed"
        val fileExtension = originalFileName.substringAfterLast('.', "jpg")
        val sanitizedFileName = sanitizeFileName(originalFileName.substringBeforeLast('.'))
        val uniqueId = UUID.randomUUID().toString().take(8)
        val fileName = "${sanitizedFileName}_${uniqueId}.$fileExtension"
        
        val file = File("$uploadDir/$fileName")
        part.streamProvider().use { input ->
            file.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        return "/images/$fileName"
    }

    private fun sanitizeFileName(fileName: String): String {
        return fileName.replace(Regex("[^a-zA-Z0-9.-]"), "_")
            .take(50) // Limit the length of the original filename
    }
}