package com.example.copsboot.infrastructure.mvc

// This is the small JSON object returned for each invalid field.
// Example: {"fieldName":"password","errorMessage":"size must be between 6 and 1000"}
data class FieldErrorResponse(
    val fieldName: String,
    val errorMessage: String
)