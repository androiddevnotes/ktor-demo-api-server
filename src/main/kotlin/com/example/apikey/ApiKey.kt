package com.example.apikey

import com.example.user.*
import kotlinx.serialization.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*

object ApiKeys : Table("api_keys") {
    val id = integer("id").autoIncrement()
    val key = varchar("key", 255).uniqueIndex()
    val userId = integer("user_id").references(Users.id)
    val createdAt = datetime("created_at")
    val lastUsedAt = datetime("last_used_at").nullable()

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class ApiKey(
    val id: Int,
    val key: String,
    val userId: Int,
    val createdAt: String,
    val lastUsedAt: String?
)