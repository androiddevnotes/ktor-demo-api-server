package com.example.repositories

import com.example.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class UserRepository {
    fun createUser(user: UserDTO): User = transaction {
        val id = Users.insert {
            it[username] = user.username
            it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
            it[role] = "USER" // Default role
        } get Users.id
        User(id, user.username, "", "USER")
    }

    fun findByUsername(username: String): User? = transaction {
        Users.select { Users.username eq username }
            .mapNotNull { toUser(it) }
            .singleOrNull()
    }

    private fun toUser(row: ResultRow): User =
        User(
            id = row[Users.id],
            username = row[Users.username],
            password = row[Users.password],
            role = row[Users.role]
        )
}