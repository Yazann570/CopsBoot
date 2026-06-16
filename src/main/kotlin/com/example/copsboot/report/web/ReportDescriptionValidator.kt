package com.example.copsboot.report.web

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ReportDescriptionValidator : ConstraintValidator<ValidReportDescription, String> {

    // This function is available if the annotation ever needs configuration.
    // We do not need configuration here, so it stays empty.
    override fun initialize(constraintAnnotation: ValidReportDescription) {
    }

    // This checks the actual description value.
    // Null is allowed here because @NotNull should be responsible for null checks.
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) {
            return true
        }

        return value.lowercase().contains("suspect")
    }
}