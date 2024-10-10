package com.example.quotes

import kotlinx.serialization.*
import org.jetbrains.exposed.sql.*

object Quotes : Table() {
  val id = integer("id").autoIncrement()
  val content = varchar("content", 1000)
  val author = varchar("author", 100)
  val imageUrl = varchar("image_url", 255).nullable()
  val category = varchar("category", 50).nullable()

  override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Quote(
  val id: Int,
  val content: String,
  val author: String,
  val imageUrl: String?,
  val category: String?,
)

@Serializable
data class QuoteDTO(
  val content: String,
  val author: String,
  val imageUrl: String? = null,
  val category: String? = null,
)
