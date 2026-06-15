package com.example.copsboot.user.web

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

// This annotation validates the whole CreateOfficerParameters object.
// We need object-level validation because duplicate-email checking needs the email value.
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [CreateUserParametersValidator::class])
annotation class ValidCreateUserParameters(
    val message: String = "Invalid user",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)