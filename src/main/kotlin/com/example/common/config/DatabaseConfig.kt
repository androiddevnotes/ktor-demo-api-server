package com.example.common.config

import org.jetbrains.exposed.sql.Database
import io.github.cdimascio.dotenv.dotenv
import java.net.URI
import org.flywaydb.core.Flyway

object DatabaseConfig {
    fun init() {
        val dotenv = dotenv {
            ignoreIfMissing = true
        }

        val dbUrl = System.getenv("DATABASE_URL") ?: dotenv["DATABASE_URL"]
        val dbUser: String
        val dbPassword: String
        val jdbcUrl: String

        if (dbUrl != null && dbUrl.startsWith("postgres://")) {
            
            val uri = URI(dbUrl)
            dbUser = uri.userInfo.split(":")[0]
            dbPassword = uri.userInfo.split(":")[1]
            jdbcUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
        } else {
            
            jdbcUrl = dbUrl ?: throw IllegalStateException("DATABASE_URL must be set")
            dbUser = System.getenv("DATABASE_USER") ?: dotenv["DATABASE_USER"] ?: throw IllegalStateException("DATABASE_USER must be set")
            dbPassword = System.getenv("DATABASE_PASSWORD") ?: dotenv["DATABASE_PASSWORD"] ?: throw IllegalStateException("DATABASE_PASSWORD must be set")
        }

        
        val flyway = Flyway.configure()
            .dataSource(jdbcUrl, dbUser, dbPassword)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)  
            .load()

        
        flyway.migrate()

        
        Database.connect(
            url = jdbcUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
    }
}