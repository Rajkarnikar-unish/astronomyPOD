package com.example.astronomypod.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.astronomypod.R
import com.example.astronomypod.databinding.ActivityMainBinding
    const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}