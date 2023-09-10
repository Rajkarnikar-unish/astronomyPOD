package com.example.astronomypod.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astronomypod.repository.PODRepository

class PodViewModelProviderFactory(
    val podRepository: PODRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PODViewModel(podRepository) as T
    }
}