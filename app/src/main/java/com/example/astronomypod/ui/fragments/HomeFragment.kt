package com.example.astronomypod.ui.fragments

import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.astronomypod.R
import com.example.astronomypod.databinding.FragmentHomeBinding
import com.example.astronomypod.ui.TAG
import com.example.astronomypod.ui.api.RetrofitInstance
import java.io.FileNotFoundException
import java.io.IOException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(34)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getPOD()
            } catch (e: IOException) {
                Log.e(TAG, "IOException: check your internet connection")
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: unexpected response received")
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                val pod = response.body()

                Log.e(TAG, "API Fetch Results===> $pod")

                binding.apply {
                    titleTextview.text = pod!!.title
                    authorTextview.text = getString(R.string.author, pod!!.copyright)
                    dateTextview.text = getString(R.string.date, pod!!.date)
                    explanationTextview.text = pod!!.explanation

                    try {
                        Glide
                            .with(this@HomeFragment)
                            .load(pod.url.toString())
                            .error(R.drawable.barnard)
                            .into(astronomyImageview)
                    } catch (e: FileNotFoundException){
                        throw e
                    }
                }

//                try {
//                    Glide
//                        .with(this@HomeFragment)
//                        .load(pod!!.url.toString())
//                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}