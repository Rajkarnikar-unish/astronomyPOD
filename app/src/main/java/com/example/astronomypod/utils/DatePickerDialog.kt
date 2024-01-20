package com.example.astronomypod.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerDialog: DialogFragment(), DatePickerDialog.OnDateSetListener {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var onDateSetListener: ((String) -> Unit)? = null
    
    fun setOnDateSetListener(listener: (String) -> Unit) {
        onDateSetListener = listener 
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        return DatePickerDialog(requireContext(), this, year, month, day)
    }
    
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val selectedDate = formatDate(year, month, day)
        onDateSetListener?.invoke(selectedDate)
    }
    
    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return dateFormat.format(calendar.time)
    }
}