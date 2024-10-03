package com.example.plugins

import com.example.models.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

fun Application.configureDatabase() {
    val driverClassName = environment.config.property("database.driverClassName").getString()
    val jdbcURL = environment.config.property("database.jdbcURL").getString()
    val user = environment.config.property("database.user").getString()
    val password = environment.config.property("database.password").getString()
    val database = Database.connect(jdbcURL, driverClassName, user, password)

    // Log the development port
    val port = environment.config.property("ktor.deployment.port").getString()
    log.info("Development port LOGGING: $port")

    transaction(database) {
        SchemaUtils.create(Quotes)
    }
}