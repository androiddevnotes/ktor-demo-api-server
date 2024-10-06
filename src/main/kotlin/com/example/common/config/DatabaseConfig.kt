package com.example.common.config

import org.jetbrains.exposed.sql.Database
import io.github.cdimascio.dotenv.dotenv

object DatabaseConfig {
    fun init() {
        val dotenv = dotenv {
            ignoreIfMissing = true
        }

        val dbUrl = System.getenv("DATABASE_URL") ?: dotenv["DATABASE_URL"] ?: throw IllegalStateException("DATABASE_URL must be set")
        val dbUser = System.getenv("DATABASE_USER") ?: dotenv["DATABASE_USER"] ?: throw IllegalStateException("DATABASE_USER must be set")
        val dbPassword = System.getenv("DATABASE_PASSWORD") ?: dotenv["DATABASE_PASSWORD"] ?: throw IllegalStateException("DATABASE_PASSWORD must be set")

        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
    }
}