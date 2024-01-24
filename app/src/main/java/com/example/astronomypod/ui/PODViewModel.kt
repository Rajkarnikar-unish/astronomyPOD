package com.example.astronomypod.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.astronomypod.PODApplication
import com.example.astronomypod.models.AstronomyPOD
import com.example.astronomypod.repository.PODRepository
import com.example.astronomypod.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class PODViewModel(
    val app: Application,
    private val podRepository: PODRepository,
) : AndroidViewModel(app) {

    val pod: MutableLiveData<Resource<AstronomyPOD>> = MutableLiveData()
    
    private val _isFav = MutableLiveData<Int>()
    val isFav : LiveData<Int>
        get() = _isFav
    
    init {
        getPOD()
    }

    fun getPOD(selectedDate: String? = null) = viewModelScope.launch {
        safePODResponseCall(selectedDate)
//        Log.i("TESTING", "${podRepository.getIsFavoriteFromTitle("Deep Nebulas: From Seagull to California")}")
    }
    
    private fun handlePODResponse(response: Response<AstronomyPOD>) : Resource<AstronomyPOD>{
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    
    private suspend fun safePODResponseCall(selectedDate: String? = null) {
        pod.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                if (selectedDate == null) {
                    val response = podRepository.getPOD()
                    _isFav.postValue(podRepository.getIsFavoriteFromTitle(response.body()!!.title))
                    pod.postValue(handlePODResponse(response))
                } else {
                    val response = podRepository.getPODWithDate(selectedDate)
                    pod.postValue(handlePODResponse(response))
                }
            } else {
                pod.postValue(Resource.Error("No Internet Connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> pod.postValue(Resource.Error("Network Failure!"))
                else -> pod.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }
    
    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<PODApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run { 
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        
        return false
    }
    
    fun savePicture(astronomyPOD: AstronomyPOD) = viewModelScope.launch {
        podRepository.upsert(astronomyPOD)
    }
    
    fun getIsFavorite(title: String) = viewModelScope.launch {
        val favorite = podRepository.getIsFavoriteFromTitle(title)
        _isFav.postValue(favorite)
    }

    fun getFavoritePicture() = podRepository.getFavoritePictures()

    fun deletePicture(astronomyPOD: AstronomyPOD) = viewModelScope.launch {
        podRepository.deletePicture(astronomyPOD)
    }

}