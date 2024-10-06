package com.example.dictionary

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*
import java.time.*

object DictionaryEntries : Table("dictionary_entries") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val definition = text("definition")
    val examples = text("examples")
    val relatedTerms = text("related_terms")
    val tags = text("tags")
    val category = varchar("category", 100)
    val languages = text("languages")
    val resources = text("resources").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class DictionaryEntry(
    val id: Int = 0,
    val name: String,
    val definition: String,
    val examples: List<String>,
    val relatedTerms: List<String>,
    val tags: List<String>,
    val category: String,
    val languages: List<String>,
    val resources: List<String>,
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

object LocalDateTimeAsStringSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)


    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}

@Serializable
data class DictionaryEntryDTO(
    val name: String,
    val definition: String,
    val examples: List<String>,
    val relatedTerms: List<String>,
    val tags: List<String>,
    val category: String,
    val languages: List<String>,
    val resources: List<String>
)