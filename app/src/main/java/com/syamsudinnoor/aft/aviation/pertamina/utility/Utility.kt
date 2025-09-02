package com.syamsudinnoor.aft.aviation.pertamina.utility


import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.marginTop
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.logging.LogManager


fun numberFormatter(number: Double): String{
    val roundingDecimal = BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP)
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(roundingDecimal)
}

fun numberFormatter(number: Int): String{
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(number)
}


fun helperSettingEditText(objValidation : (String) -> Unit) : TextWatcher{
    return object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            if(s.toString().isNotEmpty()){
                objValidation(s.toString())
            }
        }

    }
}


object TimeConverter {

    fun toReadableDateTime(timestamp: Long): String {
        // Tentukan format yang diinginkan dan Locale untuk bahasa Indonesia
        val format = SimpleDateFormat("dd MM yy, HH:mm", Locale("id", "ID"))
        return format.format(Date(timestamp))
    }

    fun toReadableDate(timestamp: Long): String {
        val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return format.format(Date(timestamp))
    }


    fun toReadableTime(timestamp: Long): String {
        val format = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        return format.format(Date(timestamp))
    }


    fun parseStringDate(dateString : String) : Date?{
        val format = SimpleDateFormat("dd:MM:yyyy", Locale("id", "ID"))
        try{
            return format.parse(dateString)
        }catch (e : Exception){
            return null
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun concatenateStringDate(date : Date, hh : String, mm : String) : Long{

        if (hh.isEmpty() || mm.isEmpty()) return System.currentTimeMillis()

        val calendar : Calendar = Calendar.getInstance()

        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, hh.toInt())
        calendar.set(Calendar.MINUTE, mm.toInt())
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis

    }
}



object DialogHolder{

    @RequiresApi(Build.VERSION_CODES.N)
    fun timeDialog(context : Context, title : String, callbackValue : (getHour : Int, getMinute:Int) -> Unit)  {
        var hour = 0
        var minute = 0
        val costumeTitle = TextView(context).apply {
            text = title
            textSize = 16f
            setPadding(0, 20, 0, 20)
            gravity = Gravity.CENTER
        }
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->
            hour = hourOfDay
            minute = minuteOfHour
            callbackValue(hour,minute)
        }

        val getCurrentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply { timeInMillis = getCurrentTime }
        val cHour = calendar.get(Calendar.HOUR_OF_DAY)
        val cMinute = calendar.get(Calendar.MINUTE)

        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePicker = TimePickerDialog(context,style, listener, cHour, cMinute, true)
        timePicker.setCustomTitle(costumeTitle)
        timePicker.show()
    }

    fun dateDialog(context: Context,callbackValue : (year : Int, month : Int, day : Int) -> Unit){
        val calender = java.util.Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        val year = calender.get(java.util.Calendar.YEAR)
        val month = calender.get(java.util.Calendar.MONTH)
        val day = calender.get(java.util.Calendar.DAY_OF_MONTH)


        val dateDialo = DatePickerDialog.OnDateSetListener{ _, yearDialog, monthDialog, dayOfMonthDialog ->
            callbackValue(yearDialog,monthDialog,dayOfMonthDialog)
        }

        val datePicker = DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,dateDialo,year,month,day)
        datePicker.show()
    }

}
