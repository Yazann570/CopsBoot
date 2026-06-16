package com.example.copsboot.report.web

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

// This annotation validates one field: the report description.
// In the book's example, a valid report description must contain the word "suspect".
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ReportDescriptionValidator::class])
annotation class ValidReportDescription(
    val message: String = "Invalid report description",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)