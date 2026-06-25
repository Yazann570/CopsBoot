package com.example.copsboot.android.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi{
    @GET("api/users/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorizaton: String
    ): Response<ResponseBody>
}