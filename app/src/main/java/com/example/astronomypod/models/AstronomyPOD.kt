package com.example.astronomypod.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "astronomyPictures"
)
data class AstronomyPOD(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val copyright: String?,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)