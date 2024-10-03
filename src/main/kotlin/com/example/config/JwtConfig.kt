package com.example.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val SECRET = "your-secret-key" // In a real application, this should be stored securely
    private const val ISSUER = "quote-app"
    private const val VALIDITY_IN_MS = 36_000_00 * 10 // 10 hours

    fun makeToken(user: com.example.models.User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(ISSUER)
        .withClaim("id", user.id)
        .withClaim("username", user.username)
        .withClaim("role", user.role)
        .withExpiresAt(getExpiration())
        .sign(Algorithm.HMAC256(SECRET))

    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_IN_MS)

    fun verifier() = JWT
        .require(Algorithm.HMAC256(SECRET))
        .withIssuer(ISSUER)
        .build()
}