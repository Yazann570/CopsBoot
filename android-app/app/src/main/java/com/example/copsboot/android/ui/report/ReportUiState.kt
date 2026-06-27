package com.example.copsboot.android.ui.report

import android.net.Uri
import com.example.copsboot.android.model.ReportDto

data class ReportUiState(
    val description: String = "",
    val trafficIncident: Boolean = false,
    val numberOfInvolvedCars: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val message: String = "",
    val createdReport: ReportDto? = null
)