package com.example.common.config

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("com.example.common.config.DatabaseConfig")

object DatabaseConfig {
    fun init(databaseUrl: String, databaseUser: String?, databasePassword: String?) {
        logger.info("Initializing database connection...")
        
        
        if (databaseUser != null && databasePassword != null) {
            Database.connect(databaseUrl, driver = "org.postgresql.Driver", user = databaseUser, password = databasePassword)
        } else {
            Database.connect(databaseUrl, driver = "org.postgresql.Driver")
        }

        
        val flyway = Flyway.configure()
            .dataSource(databaseUrl, databaseUser, databasePassword)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)  
            .load()

        flyway.migrate()

        logger.info("Flyway migration completed.")
    }
}