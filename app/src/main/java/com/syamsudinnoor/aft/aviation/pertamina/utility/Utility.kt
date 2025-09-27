package com.syamsudinnoor.aft.aviation.pertamina.utility


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.ui.main.LoginActivity


fun numberFormatter(number: Double, scale : Int = 2): String{
    val roundingDecimal = BigDecimal(number).setScale(scale, BigDecimal.ROUND_HALF_UP)
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(roundingDecimal)
}

fun numberFormatter(number: Int): String{
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(number)
}


/**
 * @param number Double
 * using for return number format : 23000 to 23,000
 */
fun formatterNumber(number: Double?): String?{
    if (number == null) return null
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)

}


fun formatterNumber(number: Int?): String?{
    if (number == null) return null
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)

}


fun helperSettingEditText(onTextHolder : (String) -> Unit = {}) : TextWatcher{
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
                onTextHolder(s.toString())
            }
        }

    }
}

fun textWatcherWithNumber(textInputEditText : TextInputEditText, onTextHolder : (String) -> Unit = {}) : TextWatcher {
    return object : TextWatcher {
        private var current = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            onTextHolder(s.toString())

            if (s.toString() != current) {
                textInputEditText.removeTextChangedListener(this)

                val cleanString = s.toString().replace(".", "") // hapus titik biar raw value
                if (cleanString.isNotEmpty()) {
                    val locale = Locale("id", "ID")
                    val numberFormat = NumberFormat.getInstance(locale)
                    val formatted = numberFormat.format(cleanString.toLong())
                    current = formatted
                    textInputEditText.setText(formatted)
                    textInputEditText.setSelection(formatted.length) // cursor di akhir
                }

                textInputEditText.addTextChangedListener(this)
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


        val dateDialog = DatePickerDialog.OnDateSetListener{ _, yearDialog, monthDialog, dayOfMonthDialog ->
            callbackValue(yearDialog,monthDialog,dayOfMonthDialog)
        }

        val datePicker = DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,dateDialog,year,month,day)
        datePicker.show()

    }

}

object TerbilangConverter {

    private val satuan = arrayOf("", "SATU", "DUA", "TIGA", "EMPAT", "LIMA", "ENAM", "TUJUH", "DELAPAN", "SEMBILAN", "SEPULUH", "SEBELAS")

    private fun convert(number: Long): String {
        return when {
            number < 12 -> satuan[number.toInt()]
            number < 20 -> satuan[(number - 10).toInt()] + " BELAS"
            number < 100 -> satuan[(number / 10).toInt()] + " PULUH " + satuan[(number % 10).toInt()]
            number < 200 -> "SERATUS " + convert(number - 100)
            number < 1000 -> satuan[(number / 100).toInt()] + " RATUS " + convert(number % 100)
            number < 2000 -> "SERIBU " + convert(number - 1000)
            number < 1_000_000 -> convert(number / 1000) + " RIBU " + convert(number % 1000)
            number < 1_000_000_000 -> convert(number / 1_000_000) + " JUTA " + convert(number % 1_000_000)
            number < 1_000_000_000_000 -> convert(number / 1_000_000_000) + " MILYAR " + convert(number % 1_000_000_000)
            else -> "" // Tambahkan triliun jika perlu
        }
    }

    fun toWords(number: Double): String {
        if (number == 0.0) {
            return "NOL RUPIAH"
        }

        // Ambil nilai absolut dan bulatkan ke integer terdekat
        val longValue = Math.abs(Math.round(number))

        val result = convert(longValue)
            .replace("\\s+".toRegex(), " ") // Hapus spasi ganda
            .trim() + " RUPIAH"

        return result.toUpperCase(Locale.ROOT)
    }
}

fun openWhatsApp(context: Context, number: String, message: String){
    val url = "https://api.whatsapp.com/send?phone=$number&text=$message"
    val uri = url.toUri()
    val pm : PackageManager = context.packageManager


    val intent = Intent(Intent.ACTION_VIEW,uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try{
        context.startActivity(intent)
    }catch (e : Exception){
        Toast.makeText(context, "Ada masalah ketika membuka WhatsApp Hubungi Manual melalui 085173005241", Toast.LENGTH_SHORT).show()
    }

}

fun settingDialogGlobal(title : String,massage: String,context : Context,onYesButtonClicked : () -> Unit){
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.costum_delete_quality_control)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val titleText : TextView = dialog.findViewById(R.id.txt_title)
    titleText.text = title
    val massageText  : TextView = dialog.findViewById(R.id.txt_massage)
    massageText.text = massage


    val yesButton : Button = dialog.findViewById(R.id.btn_yes)
    yesButton.setOnClickListener {
        onYesButtonClicked()
        dialog.dismiss()
    }
    val noButton : Button = dialog.findViewById(R.id.btn_no)
    noButton.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

