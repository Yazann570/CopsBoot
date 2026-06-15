package com.example.copsboot.user.web

import com.example.copsboot.infrastructure.SpringProfiles
import com.example.copsboot.user.User
import com.example.copsboot.user.UserId
import com.example.copsboot.user.UserService
import jakarta.validation.ValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.Optional
import java.util.UUID

@SpringBootTest
@ActiveProfiles(SpringProfiles.TEST)
class CreateUserParametersValidatorTest {

    @MockitoBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var encoder: PasswordEncoder

    @Autowired
    private lateinit var factory: ValidatorFactory

    // This test proves that the validator rejects a new user when the email already exists.
    @Test
    fun invalidIfAlreadyUserWithGivenEmail() {
        val email = "wim.deblauwe@example.com"

        `when`(userService.findUserByEmail(email))
            .thenReturn(
                Optional.of(
                    User.createOfficer(
                        UserId(UUID.randomUUID()),
                        email,
                        encoder.encode("testing1234")
                    )
                )
            )

        val validator = factory.validator

        val userParameters = CreateOfficerParameters(
            email = email,
            password = "my-secret-pwd-1234"
        )

        val violations = validator.validate(userParameters)

        assertThat(violations.map { it.propertyPath.toString() })
            .contains("email")
    }

    // This test proves that the validator accepts a new user when no user has that email.
    @Test
    fun validIfNoUserWithGivenEmail() {
        val email = "wim.deblauwe@example.com"

        `when`(userService.findUserByEmail(email))
            .thenReturn(Optional.empty())

        val validator = factory.validator

        val userParameters = CreateOfficerParameters(
            email = email,
            password = "my-secret-pwd-1234"
        )

        val violations = validator.validate(userParameters)

        assertThat(violations).isEmpty()
    }
}