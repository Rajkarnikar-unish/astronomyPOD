package com.example.astronomypod.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "astronomyPictures"
)
data class AstronomyPOD(
    val copyright: String?,
    val date: String,
    val explanation: String,
    val hdurl: String?,
    val media_type: String,
    val service_version: String,
    @PrimaryKey
    val title: String,
    val url: String?,
    val isFavorite: Int? = null
    
)