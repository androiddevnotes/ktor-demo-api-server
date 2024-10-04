package com.example.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.*
import java.time.LocalDateTime

object DictionaryEntries : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val definition = text("definition")
    val examples = text("examples")
    val relatedTerms = text("related_terms")
    val tags = text("tags")
    val category = varchar("category", 100)
    val languages = text("languages")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey = PrimaryKey(id)
}

data class DictionaryEntry(
    val id: Int = 0,
    val name: String,
    val definition: String,
    val examples: List<String>,
    val relatedTerms: List<String>,
    val tags: List<String>,
    val category: String,
    val languages: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class DictionaryEntryDTO(
    val name: String,
    val definition: String,
    val examples: List<String>,
    val relatedTerms: List<String>,
    val tags: List<String>,
    val category: String,
    val languages: List<String>
)