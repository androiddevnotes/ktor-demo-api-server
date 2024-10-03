package com.example.config

import com.example.models.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.mindrot.jbcrypt.BCrypt
import kotlin.random.Random

object DatabaseConfig {
    fun init(environment: ApplicationEnvironment) {
        val driverClassName = environment.config.property("database.driverClassName").getString()
        val jdbcURL = environment.config.property("database.jdbcURL").getString()
        val user = environment.config.property("database.user").getString()
        val password = environment.config.property("database.password").getString()
        val database = Database.connect(jdbcURL, driverClassName, user, password)

        transaction(database) {
            SchemaUtils.create(Quotes, Users)
            if (Users.selectAll().count() == 0L) {
                seedUsers()
            }
            if (Quotes.selectAll().count() == 0L) {
                seedDatabase()
            }
        }
    }

    private fun seedUsers() {
        Users.insert {
            it[username] = "admin"
            it[password] = BCrypt.hashpw("admin123", BCrypt.gensalt())
            it[role] = "ADMIN"
        }
        Users.insert {
            it[username] = "user"
            it[password] = BCrypt.hashpw("user123", BCrypt.gensalt())
            it[role] = "USER"
        }
        println("Database seeded with default users.")
    }

    private fun seedDatabase() {
        val authors = listOf("Albert Einstein", "Isaac Newton", "Nikola Tesla", "Marie Curie", "Stephen Hawking")
        val contentPrefixes = listOf("The secret of", "I believe in", "The most important aspect of", "Never underestimate", "The key to success is")
        val contentSuffixes = listOf("is perseverance", "lies in curiosity", "can be found in simplicity", "requires dedication", "is continuous learning")

        repeat(1000) { 
            Quotes.insert {
                it[content] = "${contentPrefixes.random()} ${contentSuffixes.random()}"
                it[author] = authors.random()
            }
        }
        println("Database seeded with 1000 dummy quotes.")
    }
}