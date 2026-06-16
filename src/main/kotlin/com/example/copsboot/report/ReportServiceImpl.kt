package com.example.copsboot.report

import com.example.copsboot.user.UserId
import com.example.copsboot.user.UserNotFoundException
import com.example.copsboot.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.ZonedDateTime

@Service
class ReportServiceImpl(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository
) : ReportService {

    // This is the real implementation of createReport().
    // It finds the officer who is creating the report,
    // creates a Report entity, then saves it in the database.
    override fun createReport(
        reporterId: UserId,
        dateTime: ZonedDateTime,
        description: String,
        image: MultipartFile
    ): Report {
        val reporter = userRepository.findById(reporterId)
            .orElseThrow { UserNotFoundException(reporterId) }

        val report = Report.create(
            id = reportRepository.nextId(),
            reporter = reporter,
            dateTime = dateTime,
            description = description
        )

        return reportRepository.save(report)
    }
}