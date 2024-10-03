package com.example.plugins

import com.example.models.Quotes
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val driverClassName = environment.config.property("database.driverClassName").getString()
    val jdbcURL = environment.config.property("database.jdbcURL").getString()
    val user = environment.config.property("database.user").getString()
    val password = environment.config.property("database.password").getString()
    val database = Database.connect(jdbcURL, driverClassName, user, password)
    
    transaction(database) {
        SchemaUtils.create(Quotes)
    }
}