package com.example.copsboot.android.model

data class ReportDto(
    val id: ReportIdDto?,
    val reporter: String?,
    val dateTime: String?,
    val description: String?
)

data class ReportIdDto(
    val id: String?,
    val value: String?
)