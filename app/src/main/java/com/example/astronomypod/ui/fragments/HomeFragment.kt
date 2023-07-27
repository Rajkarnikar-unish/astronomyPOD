package com.example.astronomypod.ui.fragments

import android.app.DatePickerDialog
import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.astronomypod.R
import com.example.astronomypod.databinding.FragmentHomeBinding
import com.example.astronomypod.ui.TAG
import com.example.astronomypod.ui.api.RetrofitInstance
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar

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

    fun selectDate(view: View): String {
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

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (menu.size()==0) menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.calendar -> {
                        val date = selectDate(view)
                        Log.e("DATE PICKER AFTER OK", date)
                    }
                }
                return false
            }

        })

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
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}