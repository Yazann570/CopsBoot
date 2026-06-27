package com.example.copsboot.android.data.report

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.copsboot.android.api.ApiClient
import com.example.copsboot.android.data.report.ReportResult
import com.example.copsboot.android.data.auth.AuthSession
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ReportRepository{

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createReport(
        context: Context,
        description: String,
        trafficIncident: Boolean,
        numberOfInvolvedCars: Int,
        imageUri: Uri
    ): ReportResult {
        return try {
            val authorizationHeader = AuthSession.getAuthorizationHeader()
                ?: return ReportResult.Error("You must be logged in to create a report.")
            val dateTimeBody = ZonedDateTime.now()
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                .toRequestBody("text/plain".toMediaType())
            val descriptionBody = description
                .toRequestBody("text/plain".toMediaType())
            val trafficIncidentBody = trafficIncident.toString()
                .toRequestBody("text/plain".toMediaType())

            val numberOfInvolvedCarsBody = numberOfInvolvedCars.toString()
                .toRequestBody("text/plain".toMediaType())

            val imageBytes = context.contentResolver
                .openInputStream(imageUri)
                ?.use { inputStream -> inputStream.readBytes() }
                ?: return ReportResult.Error("Could not read selected image.")

            val imageRequestBody = imageBytes.toRequestBody("image/*".toMediaType())

            val imagePart = MultipartBody.Part.createFormData(
                name = "image",
                filename = "crime-reports-image.jpg",
                body = imageRequestBody
            )

            val response = ApiClient.reportApi.createReport(
                authorization = authorizationHeader,
                dateTime = dateTimeBody,
                description = descriptionBody,
                trafficIncident = trafficIncidentBody,
                numberOfInvolvedCars = numberOfInvolvedCarsBody,
                image = imagePart
            )

            if (response.isSuccessful) {
                ReportResult.Success(response.body())
            } else {
                ReportResult.Error(
                    "Create report failed. Status code: ${response.code()}"
                )
            }
        } catch (exception: Exception){
            ReportResult.Error(
                "Network error: ${exception.message}"
            )
        }
    }
}