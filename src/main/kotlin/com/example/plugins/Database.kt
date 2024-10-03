package com.example.plugins

import com.example.models.Quotes
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val driverClassName = "org.postgresql.Driver"
    val jdbcURL = "jdbc:postgresql://localhost:5432/quotes_app_db"
    val user = "adn_user"
    val password = "adn_password"
    val database = Database.connect(jdbcURL, driverClassName, user, password)
    
    transaction(database) {
        SchemaUtils.create(Quotes)
    }
}