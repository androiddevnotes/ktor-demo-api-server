package com.example.common.utils

import com.example.dictionary.*
import com.example.quotes.*
import com.example.user.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime

object DatabaseSeeder {
    fun seedAll() {
        transaction {
            seedUsers()
            seedQuotes()
            seedDictionary()
        }
    }

    fun seedIfEmpty() {
        transaction {
            if (Users.selectAll().count() == 0L) {
                seedUsers()
            }
            if (Quotes.selectAll().count() == 0L) {
                seedQuotes()
            }
            if (DictionaryEntries.selectAll().count() == 0L) {
                seedDictionary()
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

    private fun seedQuotes() {
        val authors = listOf("Albert Einstein", "Isaac Newton", "Nikola Tesla", "Marie Curie", "Stephen Hawking")
        val contentPrefixes = listOf("The secret of", "I believe in", "The most important aspect of", "Never underestimate", "The key to success is")
        val contentSuffixes = listOf("is perseverance", "lies in curiosity", "can be found in simplicity", "requires dedication", "is continuous learning")
        val categories = listOf("Science", "Philosophy", "Motivation", "Life", "Success")

        repeat(1000) { 
            Quotes.insert {
                it[content] = "${contentPrefixes.random()} ${contentSuffixes.random()}"
                it[author] = authors.random()
                it[category] = categories.random()
                it[imageUrl] = null 
            }
        }
        println("Database seeded with 1000 dummy quotes including categories.")
    }

    private fun seedDictionary() {
        val entries = listOf(
            DictionaryEntry(
                name = "Variable",
                definition = "A container for storing data values.",
                examples = listOf("int x = 5;", "String name = \"John\";"),
                relatedTerms = listOf("Constant", "Data Type"),
                tags = listOf("Basic", "Programming Fundamentals"),
                category = "Programming Concepts",
                languages = listOf("Java", "Python", "C++"),
                resources = listOf(
                    "https://www.w3schools.com/java/java_variables.asp",
                    "https://docs.python.org/3/tutorial/introduction.html#variables"
                )
            ),
            DictionaryEntry(
                name = "Function",
                definition = "A block of organized, reusable code that performs a specific task.",
                examples = listOf("def greet(name):\n    print(f\"Hello, {name}!\")", "int add(int a, int b) {\n    return a + b;\n}"),
                relatedTerms = listOf("Method", "Procedure", "Subroutine"),
                tags = listOf("Basic", "Programming Fundamentals"),
                category = "Programming Concepts",
                languages = listOf("Python", "Java", "JavaScript"),
                resources = listOf(
                    "https://www.w3schools.com/python/python_functions.asp",
                    "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Functions"
                )
            )
        )

        entries.forEach { entry ->
            DictionaryEntries.insert {
                it[name] = entry.name
                it[definition] = entry.definition
                it[examples] = entry.examples.joinToString("|")
                it[relatedTerms] = entry.relatedTerms.joinToString("|")
                it[tags] = entry.tags.joinToString("|")
                it[category] = entry.category
                it[languages] = entry.languages.joinToString("|")
                it[resources] = entry.resources.joinToString("|")
                it[createdAt] = LocalDateTime.now()
                it[updatedAt] = LocalDateTime.now()
            }
        }
        println("Dictionary seeded with initial entries including resources.")
    }
}