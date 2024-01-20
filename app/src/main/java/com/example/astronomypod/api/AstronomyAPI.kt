package com.example.astronomypod.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.astronomypod.models.AstronomyPOD
import com.example.astronomypod.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

interface AstronomyAPI {
    
    @GET("apod")
    suspend fun getPOD(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("date") date: String
    ) : Response<AstronomyPOD>

}