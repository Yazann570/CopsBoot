package com.example.copsboot.android.ui.report

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.copsboot.android.data.report.ReportRepository
import com.example.copsboot.android.data.report.ReportResult
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportRepository: ReportRepository = ReportRepository()
): ViewModel(){

    var uiState by mutableStateOf(ReportUiState())
        private set

    fun onDescriptionChanged(isTrafficIncident: Boolean){
        uiState = uiState.copy(
            trafficIncident = isTrafficIncident,
            numberOfInvolvedCars = if(isTrafficIncident){
                uiState.numberOfInvolvedCars
            } else{
                ""
            },
            message = ""
        )
    }
    fun onImageSelected(uri: Uri?){
        uiState = uiState.copy(
            selectedImageUri = uri,
            message = ""
        )
    }
    fun createReport(context: Context){
        val description = uiState.description.trim()
        val imageUri = uiState.selectedImageUri

        if(description.isBlank()){
            uiState = uiState.copy(
                message = "Description is required."
            )
            return
        }
        if (!description.lowercase().contains("suspect")) {
            uiState = uiState.copy(
                message = "Description must contain the word suspect."
            )
            return
        }

        if (imageUri == null) {
            uiState = uiState.copy(
                message = "Please select an image."
            )
            return
        }

        val numberOfInvolvedCars = uiState.numberOfInvolvedCars.toIntOrNull() ?: 0

        if (uiState.trafficIncident && numberOfInvolvedCars <= 0) {
            uiState = uiState.copy(
                message = "Traffic incidents must involve at least one car."
            )
            return
        }
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                message = ""
            )
            val result = reportRepository.createReport(
                context = context,
                description = description,
                trafficIncident = uiState.trafficIncident,
                numberOfInvolvedCars = numberOfInvolvedCars,
                imageUri = imageUri
            )
            uiState = when(result){
                is ReportResult.Success -> {
                    ReportUiState(
                        message = "Crime report created successfully.",
                        createdReport = result.report
                    )
                }
                is ReportResult.Error -> {
                    uiState.copy(
                        isLoading = false,
                        message = result.message
                    )
                }
            }
        }
    }
}

