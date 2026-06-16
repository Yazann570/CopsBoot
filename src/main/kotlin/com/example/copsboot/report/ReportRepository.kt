package com.example.copsboot.report

import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository : JpaRepository<Report, ReportId>, ReportRepositoryCustom