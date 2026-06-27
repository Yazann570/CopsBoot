package com.example.copsboot.android.api

import com.example.copsboot.android.model.ReportDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ReportApi{

    @Multipart
    @POST("api/reports")
    suspend fun createReport(
        @Header("Authorization") authorization: String,
        @Part("dateTime") dateTime: RequestBody,
        @Part("description") description: RequestBody,
        @Part("trafficIncident") trafficIncident: RequestBody,
        @Part("numberOfInvolvedCars") numberOfInvolvedCars: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ReportDto>
}