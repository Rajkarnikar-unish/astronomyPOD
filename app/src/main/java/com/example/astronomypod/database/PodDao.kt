package com.example.astronomypod.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.astronomypod.models.AstronomyPOD

@Dao
interface PodDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(astronomyPOD: AstronomyPOD)

    @Transaction
    suspend fun upsert(astronomyPOD: AstronomyPOD) {
        val pod = astronomyPOD.copy(isFavorite = astronomyPOD.isFavorite ?: 1)
        insert(pod)
    }

    @Query("UPDATE astronomyPictures SET isFavorite = :isFavorite WHERE title=:title")
    suspend fun toggleFavorite(title: String, isFavorite: Int): Int
    
    @Query("SELECT isFavorite FROM astronomyPictures WHERE title = :title")
    suspend fun getIsFavoriteFromTitle(title: String) : Int
    
    @Query("SELECT * FROM astronomyPictures WHERE isFavorite = 1")
    fun getFavoritePictures(): LiveData<List<AstronomyPOD>>

    @Delete
    suspend fun deletePicture(astronomyPOD: AstronomyPOD)

}