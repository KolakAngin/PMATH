package com.syamsudinnoor.aft.aviation.pertamina.utility


import android.icu.util.Calendar
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
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