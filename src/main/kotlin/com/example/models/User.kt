package com.example.models

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 100)
    val role = varchar("role", 20)

    override val primaryKey = PrimaryKey(id)
}

data class User(val id: Int, val username: String, val password: String, val role: String)
data class UserDTO(val username: String, val password: String)