package com.example.astronomypod.ui.api

import com.example.astronomypod.ui.models.AstronomyPOD
import com.example.astronomypod.ui.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AstronomyAPI {

    @GET("apod")
    suspend fun getPOD(
        @Query("api_key") apiKey: String = API_KEY
    ) : Response<AstronomyPOD>

    @GET("apod")
    suspend fun getPictureWithDate(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("date") date: String,
    ): Response<AstronomyPOD>

}