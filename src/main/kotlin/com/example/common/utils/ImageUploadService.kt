package com.example.common.utils

import java.io.File
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import java.util.*

class ImageUploadService(private val uploadDir: String) {
    private val allowedExtensions = listOf("jpg", "jpeg", "png", "gif")
    private val allowedContentTypes = listOf(
        ContentType.Image.JPEG,
        ContentType.Image.PNG,
        ContentType.Image.GIF
    )

    init {
        File(uploadDir).mkdirs()
    }

    fun saveImage(part: PartData.FileItem): String {
        val originalFileName =
            part.originalFileName ?: throw BadRequestException("Original file name is missing")
        val fileExtension =
            originalFileName.substringAfterLast('.', "").lowercase(Locale.getDefault())

        if (fileExtension !in allowedExtensions) {
            throw BadRequestException(
                "File type not allowed. Allowed types are: ${
                    allowedExtensions.joinToString(
                        ", "
                    )
                }"
            )
        }

        if (part.contentType !in allowedContentTypes) {
            throw BadRequestException(
                "Content type not allowed. Allowed types are: ${
                    allowedContentTypes.joinToString(
                        ", "
                    )
                }"
            )
        }

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
            .take(50) 
    }
}