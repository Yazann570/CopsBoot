package com.example.copsboot.report.web

import com.example.copsboot.infrastructure.security.userId
import com.example.copsboot.report.ReportService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
class ReportRestController(
    private val service: ReportService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createReport(
        @AuthenticationPrincipal jwt: Jwt,
        @Valid parameters: CreateReportParameters
    ): ReportDto{
        val report = service.createReport(
            reporterId = jwt.userId(),
            dateTime = requireNotNull(parameters.dateTime),
            description = requireNotNull(parameters.description),
            image = requireNotNull(parameters.image)
        )
        return ReportDto.fromReport(report)
    }
}