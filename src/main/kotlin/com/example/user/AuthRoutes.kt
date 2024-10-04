package com.example.user

import com.example.common.config.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.common.utils.respondError

fun Route.authRoutes(userService: UserService) {
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
    }
}