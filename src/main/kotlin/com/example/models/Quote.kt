package com.example.models

import org.jetbrains.exposed.sql.*

object Quotes : Table() {
    val id = integer("id").autoIncrement()
    val content = varchar("content", 1000)
    val author = varchar("author", 100)
    val imageUrl = varchar("image_url", 255).nullable() // Add this line

    override val primaryKey = PrimaryKey(id)
}

data class Quote(val id: Int, val content: String, val author: String, val imageUrl: String?)