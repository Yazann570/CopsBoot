package com.example.copsboot.report.web

import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.time.ZonedDateTime

@ValidCreateReportParameters
data class CreateReportParameters(
    @field:NotNull
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val dateTime: ZonedDateTime? = null,

    @field:NotNull
    @field:ValidReportDescription
    val description: String? = null,

    val trafficIncident: Boolean = false,

    val numberOfInvolvedCars: Int = 0,

    @field:NotNull
    val image: MultipartFile? = null
)