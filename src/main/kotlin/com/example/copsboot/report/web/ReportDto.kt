package com.example.copsboot.report.web

import com.example.copsboot.report.Report
import com.example.copsboot.report.ReportId
import java.time.ZonedDateTime

data class ReportDto(
    val id: ReportId,
    val reporter: String,
    val dateTime: ZonedDateTime,
    val description: String
){
    companion object{
        fun fromReport(report: Report): ReportDto{
            return ReportDto(
                id = report.getId(),
                reporter = report.reporter.email,
                dateTime = report.dateTime,
                description = report.description
            )
        }
    }
}