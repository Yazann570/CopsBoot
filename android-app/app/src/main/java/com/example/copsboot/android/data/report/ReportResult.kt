package com.example.copsboot.android.data

import com.example.copsboot.android.model.ReportDto

sealed class ReportResult{
    data class Success(
        val report: ReportDto?
    ): ReportResult()

    data class Error(
        val message: String
    ): ReportResult()
}