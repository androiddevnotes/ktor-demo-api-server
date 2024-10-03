package com.example

import com.example.config.*
import com.example.plugins.*
import com.example.repositories.*
import com.example.routes.*
import com.example.services.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import com.example.services.ImageUploadService
import io.ktor.server.http.content.*
import java.io.File
import io.ktor.server.plugins.statuspages.*
import com.example.utils.respondError
import io.ktor.http.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseConfig.init(environment)
    val quoteRepository = QuoteRepository()
    val quoteService = QuoteService(quoteRepository)
    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    val uploadDir = environment.config.property("upload.dir").getString()
    val imageUploadService = ImageUploadService(uploadDir)

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalArgumentException -> call.respondError(
                    HttpStatusCode.BadRequest,
                    cause.message ?: "Bad Request",
                    "BAD_REQUEST"
                )
                else -> call.respondError(
                    HttpStatusCode.InternalServerError,
                    "An internal error occurred",
                    "INTERNAL_SERVER_ERROR"
                )
            }
        }
    }

    configureSerialization()
    configureRouting(quoteService, userService, imageUploadService)
}

fun Application.configureRouting(quoteService: QuoteService, userService: UserService, imageUploadService: ImageUploadService) {
    routing {
        authRoutes(userService)
        quoteRoutes(quoteService, imageUploadService)
        static("/images") {
            files(File(environment?.config?.property("upload.dir")?.getString() ?: ""))
        }
    }
}
