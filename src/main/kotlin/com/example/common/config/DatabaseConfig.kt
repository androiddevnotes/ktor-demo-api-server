package com.example.common.config

import com.example.dictionary.*
import com.example.quotes.*
import com.example.user.*
import com.example.common.utils.DatabaseSeeder
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

object DatabaseConfig {
    fun init(environment: ApplicationEnvironment) {
        val driverClassName = environment.config.property("database.driverClassName").getString()
        val jdbcURL = environment.config.property("database.jdbcURL").getString()
        val user = environment.config.property("database.user").getString()
        val password = environment.config.property("database.password").getString()
        val database = Database.connect(jdbcURL, driverClassName, user, password)

        transaction(database) {
            SchemaUtils.create(Quotes, Users, DictionaryEntries)
            
            // Use the new seeder utility
            DatabaseSeeder.seedIfEmpty()
        }
    }
}