package com.example.services

import java.io.File
import java.util.UUID
import io.ktor.http.content.*

class ImageUploadService(private val uploadDir: String) {
    init {
        File(uploadDir).mkdirs()
    }

    fun saveImage(part: PartData.FileItem): String {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val file = File("$uploadDir/$fileName")
        part.streamProvider().use { input ->
            file.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        return "/images/$fileName"
    }
}