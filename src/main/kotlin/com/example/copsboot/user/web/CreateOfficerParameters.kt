package com.example.copsboot.user.web

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateOfficerParameters(
    @field:NotNull
    @field:Email
    val email: String? = null,
    @field:NotNull
    @field:Size(min = 6, max=1000)
    val password: String? = null
)