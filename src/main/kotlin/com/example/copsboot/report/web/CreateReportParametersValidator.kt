package com.example.copsboot.report.web

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CreateReportParametersValidator :
    ConstraintValidator<ValidCreateReportParameters, CreateReportParameters> {

    // This function is available if the annotation ever needs configuration.
    // We do not need configuration here, so it stays empty.
    override fun initialize(constraintAnnotation: ValidCreateReportParameters) {
    }

    // This validates the relationship between trafficIncident and numberOfInvolvedCars.
    // If it is a traffic incident, at least one car must be involved.
    override fun isValid(
        value: CreateReportParameters?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) {
            return true
        }

        return !value.trafficIncident || value.numberOfInvolvedCars > 0
    }
}