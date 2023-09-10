package com.example.astronomypod.ui.fragments

import android.app.DatePickerDialog
import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.astronomypod.R
import com.example.astronomypod.databinding.FragmentHomeBinding
import com.example.astronomypod.ui.TAG
import com.example.astronomypod.api.RetrofitInstance
import com.example.astronomypod.ui.MainActivity
import com.example.astronomypod.ui.PODViewModel
import com.example.astronomypod.ui.PodViewModelProviderFactory
import com.example.astronomypod.utils.Resource
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar

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

    fun selectDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var finalDate = "$year-$month-$day"


        val datePicker = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYr, dayOfMonth ->
                finalDate = "$year-${monthOfYr+1}-$dayOfMonth"
        }, year, month, day,)
        datePicker.show()
        return finalDate
    }

    @RequiresApi(34)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val menuHost: MenuHost = requireActivity()
//
//        menuHost.addMenuProvider(object : MenuProvider {
//
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                if (menu.size()==0) menuInflater.inflate(R.menu.menu, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                when(menuItem.itemId) {
//                    R.id.calendar -> {
//                        val date = selectDate()
//                        Log.e("DATE PICKER AFTER OK", date)
//                    }
//                }
//                return false
//            }
//
//        })

        podViewModel = (activity as MainActivity).podViewModel

        podViewModel.pod.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { podResponse ->
                        val picture = podResponse
                        binding.apply {
                            titleTextview.text = podResponse.title
                            authorTextview.text = if (podResponse.copyright.isNotEmpty()) getString(
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
                                podViewModel.savePicture(picture)
                                Snackbar.make(it, "Picture is stored successfully.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("HomeFragment", "An Error Occured: $message" )
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

//                    astronomyImageview.setOnClickListener {
//                        Log.e(TAG, "ImageView Clicked!")
////                        fullscreen(pod.hdurl)

    private fun hideProgressBar() {
        binding.podProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.podProgressBar.visibility = View.VISIBLE
    }

//    fun fullscreen(url: String) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}