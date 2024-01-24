package com.example.astronomypod.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.astronomypod.R
import com.example.astronomypod.databinding.FragmentHomeBinding
import com.example.astronomypod.ui.MainActivity
import com.example.astronomypod.ui.PODViewModel
import com.example.astronomypod.utils.Resource
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var podViewModel: PODViewModel   
    
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
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        podViewModel = (activity as MainActivity).podViewModel

        populateUI(podViewModel)
        podViewModel.isFav.observe(viewLifecycleOwner, Observer { 
            Log.e("FRAGMENT", "isFavorite: $it")
            if(it == 1) {
//                binding.favoriteFAB.setImageDrawable()
            }
        })
        
        val menuHost: MenuHost = requireActivity()
        
//        podViewModel.isFav.observe(viewLifecycleOwner, Observer { 
//            println(it)
//        })
        
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if(menu.size()==0) menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.calendar -> {
                        val datePicker = com.example.astronomypod.utils.DatePickerDialog()
                        datePicker.setOnDateSetListener {
                            podViewModel.getPOD(it)
                        }
                        datePicker.show(requireFragmentManager(), "datePicker")
                    }
                }
                return false
            }

        })
    }
    
    private fun populateUI(podViewModel: PODViewModel) {
        podViewModel.pod.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { podResponse ->
//                        println(podResponse.isFavorite)
                        binding.apply {
                            titleTextview.text = podResponse.title
                            authorTextview.text = if (podResponse.copyright != null) getString(
                                R.string.author,
                                podResponse.copyright
                            ) else ""
                            dateTextview.text = getString(R.string.date, podResponse.date)
                            explanationTextview.text = podResponse.explanation
                            try {
                                Glide
                                    .with(this@HomeFragment)
                                    .load(podResponse.url.toString())
                                    .error(R.drawable.barnard)
                                    .into(astronomyImageview)
                            } catch (e: FileNotFoundException) {
                                throw e
                            }
                            
                            binding.favoriteFAB.setOnClickListener {
                                podViewModel.savePicture(podResponse)
                                Snackbar.make(it, "Picture is stored successfully.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.favoriteFAB.visibility = View.INVISIBLE
                        binding.noInternetLayout.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> showProgressBar()
            }
        })
    }

    private fun hideProgressBar() {
        binding.podProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.podProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

//    private fun fullscreen(url: String) {
//        val dialog = Dialog(
//            requireActivity(),
//            android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
//        )
//
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.fullscreen_imageview)
//
//        val imageView = dialog.findViewById<ImageView>(R.id.fullscreen_imageview)
//        val closeBtn = dialog.findViewById<Button>(R.id.close_button)
//        imageView.setBackgroundColor(resources.getColor(R.color.black))
//        Glide
//            .with(this@HomeFragment)
//            .load(url.toString())
//            .into(imageView)
//
//        closeBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }