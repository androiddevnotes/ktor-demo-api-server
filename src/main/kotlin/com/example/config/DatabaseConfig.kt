package com.example.config

import com.example.models.*
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
            SchemaUtils.create(Quotes)
        }
    }
}