package com.example.copsboot.user

import org.springframework.security.crypto.factory.PasswordEncoderFactories
import java.util.UUID

object Users{
    private val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    const val OFFICER_EMAIL = "officer@example.com"
    const val OFFICER_PASSWORD = "officer"

    const val CAPTAIN_EMAIL = "captain@example.com"
    const val CAPTAIN_PASSWORD = "captain"

    private fun encodePassword(rawPassword: String): String {
        return requireNotNull(passwordEncoder.encode(rawPassword))
    }

    private val officerUser = User.createOfficer(
        newRandomId(),
        OFFICER_EMAIL,
        encodePassword(OFFICER_PASSWORD)
    )

    private val captainUser = User.createCaptain(
        newRandomId(),
        CAPTAIN_EMAIL,
        encodePassword(CAPTAIN_PASSWORD)
    )

    fun newRandomId(): UserId{
        return UserId(UUID.randomUUID())
    }
    fun newRandomOfficer(): User{
        return newRandomOfficer(newRandomId())
    }

    fun newRandomOfficer(userId: UserId): User{
        val uniqueId = userId.asString().substring(0,5)
        return User.createOfficer(
            userId,
            "user-$uniqueId@example.com",
            encodePassword("user")
        )
    }
    fun officer(): User{
        return officerUser
    }
    fun captain(): User{
        return captainUser
    }

    fun newOfficer(email: String, password: String): User {
        return User.createOfficer(
            newRandomId(),
            email,
            encodePassword(password)
        )
    }
}