package com.example.astronomypod.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.astronomypod.api.RetrofitInstance
import com.example.astronomypod.database.AstronomyDatabase
import com.example.astronomypod.models.AstronomyPOD

class PODRepository(
    val db: AstronomyDatabase
) {
    
    suspend fun getPOD(date: String) = RetrofitInstance.api.getPOD(date = date)
//    suspend fun getPOD( date: String) = RetrofitInstance.api.getPOD(date = date)

    suspend fun upsert(astronomyPOD: AstronomyPOD) = db.getPodDao().upsert(astronomyPOD)

    fun getSavedPictures() = db.getPodDao().getAllPictures()

    suspend fun deletePicture(astronomyPOD: AstronomyPOD) = db.getPodDao().deletePicture(astronomyPOD)
}