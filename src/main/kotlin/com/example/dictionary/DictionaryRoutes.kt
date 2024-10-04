package com.example.dictionary

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.dictionaryRoutes(dictionaryService: DictionaryService) {
    route("/api/v1/dictionary") {
        get {
            call.respond(dictionaryService.getAllEntries())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val entry = dictionaryService.getEntryById(id)
            if (entry != null) {
                call.respond(entry)
            } else {
                call.respond(HttpStatusCode.NotFound, "Entry not found")
            }
        }

        get("/search") {
            val query = call.request.queryParameters["q"] ?: ""
            call.respond(dictionaryService.searchEntries(query))
        }

        authenticate {
            post {
                val entryDTO = call.receive<DictionaryEntryDTO>()
                val createdEntry = dictionaryService.createEntry(entryDTO)
                call.respond(HttpStatusCode.Created, createdEntry)
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@put
                }

                val entryDTO = call.receive<DictionaryEntryDTO>()
                if (dictionaryService.updateEntry(id, entryDTO)) {
                    call.respond(HttpStatusCode.OK, "Entry updated successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Entry not found")
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }

                if (dictionaryService.deleteEntry(id)) {
                    call.respond(HttpStatusCode.OK, "Entry deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Entry not found")
                }
            }
        }
    }
}