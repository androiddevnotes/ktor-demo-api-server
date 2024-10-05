package com.example.common.config

import com.example.dictionary.*
import com.example.quotes.*
import com.example.user.*
import com.example.common.utils.DatabaseSeeder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import java.net.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("com.example.common.config.DatabaseConfig")

object DatabaseConfig {
    fun init(databaseUrl: String, databaseUser: String?, databasePassword: String?) {
        logger.info("Initializing database connection...")
        val dbUrl = System.getenv("DATABASE_URL")
        if (dbUrl != null) {
            val dbUri = URI(dbUrl)
            val username = dbUri.userInfo.split(":")[0]
            val password = dbUri.userInfo.split(":")[1]
            val jdbcUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}"
            Database.connect(
                jdbcUrl,
                driver = "org.postgresql.Driver",
                user = username,
                password = password
            )
        } else if (databaseUser != null && databasePassword != null) {
            Database.connect(databaseUrl, user = databaseUser, password = databasePassword)
        } else {
            throw IllegalStateException("Database configuration is missing")
        }

        transaction {
            SchemaUtils.create(Quotes, Users, DictionaryEntries)
            logger.info("Database schema created successfully")
            
            
            DatabaseSeeder.seedIfEmpty()
            logger.info("Database seeded successfully")
        }
        logger.info("Database initialization completed")
    }
}