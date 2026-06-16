package com.example.copsboot.report.web

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

// This annotation validates the whole CreateReportParameters object.
// We need object-level validation because trafficIncident and numberOfInvolvedCars depend on each other.
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [CreateReportParametersValidator::class])
annotation class ValidCreateReportParameters(
    val message: String = "Invalid report",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)