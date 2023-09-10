package com.example.astronomypod.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.astronomypod.models.AstronomyPOD

@Dao
interface PodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(astronomyPOD: AstronomyPOD): Long

    @Query("SELECT * FROM astronomyPictures")
    fun getAllPictures(): LiveData<List<AstronomyPOD>>

    @Delete
    suspend fun deletePicture(astronomyPOD: AstronomyPOD)

}