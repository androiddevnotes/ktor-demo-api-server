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
import com.example.exceptions.*

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
                is NotFoundException -> call.respondError(
                    HttpStatusCode.NotFound,
                    cause.message ?: "Resource not found",
                    "NOT_FOUND"
                )
                is UnauthorizedException -> call.respondError(
                    HttpStatusCode.Unauthorized,
                    cause.message ?: "Unauthorized",
                    "UNAUTHORIZED"
                )
                is ForbiddenException -> call.respondError(
                    HttpStatusCode.Forbidden,
                    cause.message ?: "Forbidden",
                    "FORBIDDEN"
                )
                is ConflictException -> call.respondError(
                    HttpStatusCode.Conflict,
                    cause.message ?: "Conflict",
                    "CONFLICT"
                )
                else -> {
                    call.application.log.error("Unhandled exception", cause)
                    call.respondError(
                        HttpStatusCode.InternalServerError,
                        "An internal error occurred",
                        "INTERNAL_SERVER_ERROR"
                    )
                }
            }
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondError(
                HttpStatusCode.NotFound,
                "The requested resource was not found",
                "NOT_FOUND"
            )
        }
        status(HttpStatusCode.MethodNotAllowed) { call, _ ->
            call.respondError(
                HttpStatusCode.MethodNotAllowed,
                "The method is not allowed for the requested URL",
                "METHOD_NOT_ALLOWED"
            )
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
