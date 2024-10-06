package com.example.common.config

import org.jetbrains.exposed.sql.Database
import io.github.cdimascio.dotenv.dotenv
import java.net.URI

object DatabaseConfig {
    fun init() {
        val dotenv = dotenv {
            ignoreIfMissing = true
        }

        val dbUrl = System.getenv("DATABASE_URL") ?: dotenv["DATABASE_URL"]
        if (dbUrl != null && dbUrl.startsWith("postgres://")) {
            // Heroku-style DATABASE_URL
            val uri = URI(dbUrl)
            val (username, password) = uri.userInfo.split(":")
            val jdbcUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
            Database.connect(
                url = jdbcUrl,
                driver = "org.postgresql.Driver",
                user = username,
                password = password
            )
        } else {
            // Local development configuration
            val dbUser = System.getenv("DATABASE_USER") ?: dotenv["DATABASE_USER"] ?: throw IllegalStateException("DATABASE_USER must be set")
            val dbPassword = System.getenv("DATABASE_PASSWORD") ?: dotenv["DATABASE_PASSWORD"] ?: throw IllegalStateException("DATABASE_PASSWORD must be set")
            val jdbcUrl = dbUrl ?: throw IllegalStateException("DATABASE_URL must be set")

            Database.connect(
                url = jdbcUrl,
                driver = "org.postgresql.Driver",
                user = dbUser,
                password = dbPassword
            )
        }
    }
}