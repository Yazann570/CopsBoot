package com.example.copsboot.user.web

import com.example.copsboot.user.UserService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.Optional

class CreateUserParametersValidator(
    private val userService: UserService
) : ConstraintValidator<ValidCreateUserParameters, CreateOfficerParameters> {

    // This function is available if the annotation ever needs configuration.
    // We do not need any configuration for this validator, so it stays empty.
    override fun initialize(constraintAnnotation: ValidCreateUserParameters) {
    }

    // This performs the duplicate-email check.
    // If another user already has the requested email, we add a validation error on the email field.
    override fun isValid(
        userParameters: CreateOfficerParameters?,
        context: ConstraintValidatorContext
    ): Boolean {
        val email = userParameters?.email ?: return true

        val existingUser = userService.findUserByEmail(email) ?: Optional.empty()

        if (existingUser.isPresent) {
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate(
                    "There is already a user with the given email address."
                )
                .addPropertyNode("email")
                .addConstraintViolation()

            return false
        }

        return true
    }
}