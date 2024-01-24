package com.example.astronomypod.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.astronomypod.api.RetrofitInstance
import com.example.astronomypod.database.AstronomyDatabase
import com.example.astronomypod.models.AstronomyPOD

class PODRepository(
    val db: AstronomyDatabase
) {
    
    suspend fun getPOD() = RetrofitInstance.api.getPOD()
    suspend fun getPODWithDate(date: String) = RetrofitInstance.api.getPODWithDate(date = date)

    suspend fun upsert(astronomyPOD: AstronomyPOD) = db.getPodDao().upsert(astronomyPOD)
    
    suspend fun toggleFavorite(title: String, isFavorite: Int): Int = db.getPodDao().toggleFavorite(title, isFavorite)
    
    suspend fun getIsFavoriteFromTitle(title: String)  = db.getPodDao().getIsFavoriteFromTitle(title) 
    
    fun getFavoritePictures() = db.getPodDao().getFavoritePictures()

    suspend fun deletePicture(astronomyPOD: AstronomyPOD) = db.getPodDao().deletePicture(astronomyPOD)
}