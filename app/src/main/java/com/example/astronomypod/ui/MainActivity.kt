package com.example.astronomypod.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.example.astronomypod.R
import com.example.astronomypod.database.AstronomyDatabase
import com.example.astronomypod.databinding.ActivityMainBinding
import com.example.astronomypod.models.AstronomyPOD
import com.example.astronomypod.repository.PODRepository

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var podViewModel: PODViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val podRepository = PODRepository(AstronomyDatabase(this))

        val podViewModelFactory = PodViewModelProviderFactory(podRepository)
        podViewModel = ViewModelProvider(this, podViewModelFactory)[PODViewModel::class.java]
    }
}