package com.example.user

import com.example.apikey.*
import com.example.common.config.*
import com.example.common.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(userService: UserService, apiKeyRepository: ApiKeyRepository) {
    route("/api/v1") {
        post("/register") {
            val user = call.receive<UserDTO>()
            val registeredUser = userService.registerUser(user)
            call.respond(HttpStatusCode.Created, registeredUser)
        }

        post("/login") {
            val user = call.receive<UserDTO>()
            val validatedUser = userService.validateUser(user.username, user.password)
            if (validatedUser != null) {
                val token = JwtConfig.makeToken(validatedUser)
                call.respond(hashMapOf("token" to token))
            } else {
                call.respondError(
                    HttpStatusCode.Unauthorized,
                    "Invalid credentials",
                    "INVALID_CREDENTIALS"
                )
            }
        }

        authenticate {
            post("/api-key") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("id", Int::class)
                if (userId != null) {
                    val apiKey = apiKeyRepository.createApiKey(userId)
                    call.respond(HttpStatusCode.Created, mapOf("apiKey" to apiKey.key))
                } else {
                    call.respondError(
                        HttpStatusCode.Unauthorized,
                        "Invalid user",
                        "INVALID_USER"
                    )
                }
            }
        }
    }
}