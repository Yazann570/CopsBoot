package com.example.copsboot

import com.example.copsboot.infrastructure.SpringProfiles
import com.example.copsboot.user.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(SpringProfiles.DEV)
class DevelopmentDbInitializer(
    private val userService: UserService
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        createTestUsers()
    }

    private fun createTestUsers() {
        userService.createOfficer("officer@example.com", "officer")
    }
}