package com.example.astronomypod.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astronomypod.PODApplication
import com.example.astronomypod.models.AstronomyPOD
import com.example.astronomypod.repository.PODRepository
import com.example.astronomypod.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PODViewModel(
    val podRepository: PODRepository,
) : ViewModel() {

    val pod: MutableLiveData<Resource<AstronomyPOD>> = MutableLiveData()

    init {
        getPOD()
    }

    fun getPOD() = viewModelScope.launch {
        pod.postValue(Resource.Loading())

        val response = podRepository.getPOD()
        pod.postValue(handlePODResponse(response))
    }

    private fun handlePODResponse(response: Response<AstronomyPOD>) : Resource<AstronomyPOD>{
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun savePicture(astronomyPOD: AstronomyPOD) = viewModelScope.launch {
        podRepository.upsert(astronomyPOD)
    }

    fun getSavedPicture() = podRepository.getSavedPictures()

    fun deletePicture(astronomyPOD: AstronomyPOD) = viewModelScope.launch {
        podRepository.deletePicture(astronomyPOD)
    }

}