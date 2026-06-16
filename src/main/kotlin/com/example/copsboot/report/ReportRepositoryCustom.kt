package com.example.copsboot.report

interface ReportRepositoryCustom {

    // Generates the next strongly typed ReportId.
    fun nextId(): ReportId
}