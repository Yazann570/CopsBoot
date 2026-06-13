package com.example.copsboot.user.web

import com.example.copsboot.user.UserId
import com.example.copsboot.user.UserNotFoundException
import com.example.copsboot.user.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserRestController(
    private val service: UserService
) {
    @GetMapping("/me")
    fun currentUser(@AuthenticationPrincipal jwt: Jwt): UserDto{
        val userId = jwt.userId()
        val user = service.getUser(userId).orElseThrow{UserNotFoundException(userId)}

        return UserDto.fromUser(user)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOfficer(
        @Valid
        @RequestBody
        parameters: CreateOfficerParameters
    ): UserDto{
        val email = requireNotNull(parameters.email)
        val password = requireNotNull(parameters.password)

        val officer = service.createOfficer(email, password)
        return UserDto.fromUser(officer)

    }

    private fun Jwt.userId(): UserId{
        val userId = getClaimAsString("user_id") ?:
        throw IllegalStateException("JWT does not contain user_id claim")

        return UserId(UUID.fromString(userId))
    }
}
