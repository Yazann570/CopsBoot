package com.example.copsboot.infrastructure.security

import com.example.copsboot.user.UserId
import org.springframework.security.oauth2.jwt.Jwt
import java.util.UUID

// This extracts the current user's ID from the JWT.
// Your modern security setup stores the user ID in the "user_id" claim.
fun Jwt.userId(): UserId {
    val userId = getClaimAsString("user_id")
        ?: throw IllegalStateException("JWT does not contain a user_id claim")

    return UserId(UUID.fromString(userId))
}