package com.example.models

import org.jetbrains.exposed.sql.Table

object Quotes : Table() {
    val id = integer("id").autoIncrement()
    val content = varchar("content", 1000)
    val author = varchar("author", 100)

    override val primaryKey = PrimaryKey(id)
}

data class Quote(val id: Int, val content: String, val author: String)