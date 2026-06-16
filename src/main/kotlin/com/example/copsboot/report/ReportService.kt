package com.example.copsboot.report

import com.example.copsboot.user.UserId
import org.springframework.web.multipart.MultipartFile
import java.time.ZonedDateTime

interface ReportService {

    // This function says:
    // "Any ReportService must know how to create a report."
    // The interface does not contain the actual database logic.
    fun createReport(
        reporterId: UserId,
        dateTime: ZonedDateTime,
        description: String,
        image: MultipartFile
    ): Report
}