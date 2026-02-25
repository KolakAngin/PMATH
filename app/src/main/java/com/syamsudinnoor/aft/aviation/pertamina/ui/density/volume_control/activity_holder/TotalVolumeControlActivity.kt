package com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.activity_holder

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.adapter.AnalyitcsVolumeControlAdapter
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityTotalVolumeControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityVolumeControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FilterDateBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FilterTimeBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlMainViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.dateDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import java.util.Calendar
import java.util.Date
import androidx.core.graphics.drawable.toDrawable

class TotalVolumeControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTotalVolumeControlBinding
    private val viewModel : VolumeControlMainViewModel by viewModels{
        val database = MainDatabase.getDatabase(this)
        val dao = MainRepository(database.getDao())
        MainViewModelFactory(dao)
    }


    private lateinit var adapter: AnalyitcsVolumeControlAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTotalVolumeControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        setSupportActionBar(binding.topAppBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        adapter = AnalyitcsVolumeControlAdapter()
        binding.rvDataTotalVolume.adapter = adapter
        binding.rvDataTotalVolume.layoutManager = LinearLayoutManager(this)
        supportActionBar?.title = "Total Volume Control"

        viewModel.analyticsVolumeControl.observe(this){
            adapter.submitList(it)
            if (it.isEmpty()){
                binding.txtPilihTanggal.visibility = View.VISIBLE
            }else{
                binding.txtPilihTanggal.visibility = View.GONE
            }
            Log.d("Data Filter", "onCreate: $it")
        }

        binding.buttonFilter.setOnClickListener {
            settingDialogFilter()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun settingDialogFilter(){
        val localBinding = FilterDateBinding.inflate(layoutInflater)
        var startYear : String = ""
        var startMonth : String = ""
        var startDay : String = ""

        var endYear : String = ""
        var endMonth : String = ""
        var endDay : String = ""




        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(localBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        localBinding.startButtonDate.setOnClickListener {
            dateDialog(this){ yearDialog, monthDialog, dayOfMonthDialog ->
                startYear = yearDialog.toString()
                startMonth = (monthDialog + 1).toString()
                startDay = dayOfMonthDialog.toString()
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, yearDialog)
                    set(Calendar.MONTH, monthDialog)
                    set(Calendar.DAY_OF_MONTH, dayOfMonthDialog)
                }
                localBinding.startButtonDate.text = TimeConverter.toReadableDate(calendar.timeInMillis)
            }
        }

        localBinding.endButtonDate.setOnClickListener {
            dateDialog(this){ yearDialog, monthDialog, dayOfMonthDialog ->
                endYear = yearDialog.toString()
                endMonth = (monthDialog + 1).toString()
                endDay = dayOfMonthDialog.toString()
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, yearDialog)
                    set(Calendar.MONTH, monthDialog)
                    set(Calendar.DAY_OF_MONTH, dayOfMonthDialog)
                }
                localBinding.endButtonDate.text = TimeConverter.toReadableDate(calendar.timeInMillis)
            }
        }

        localBinding.buttonFilter.setOnClickListener {
            if ( startYear.isNotEmpty() && startMonth.isNotEmpty() && startDay.isNotEmpty()
                && endYear.isNotEmpty() && endMonth.isNotEmpty() && endDay.isNotEmpty()){
                val startDate : Date? = TimeConverter.parseStringDate("$startDay:$startMonth:$startYear")
                val endDate : Date? = TimeConverter.parseStringDate("$endDay:$endMonth:$endYear")

                val startTime : Calendar = Calendar.getInstance()
                val endTime : Calendar = Calendar.getInstance()

                if (startDate != null && endDate != null){
                    startTime.time = startDate
                    endTime.time = endDate
                    viewModel.getAnalyticsVolumeControl(startTime.timeInMillis,endTime.timeInMillis + ONE_DAY)
                }
            }else{
                Toast.makeText(this, "Argumen Tidak Valid", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()

        }

        dialog.show()

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val margin = (24 * context.resources.displayMetrics.density).toInt()
            setBackgroundDrawable(
                InsetDrawable(Color.TRANSPARENT.toDrawable(),margin)
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when{
            item.itemId == android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    companion object{
        private const val ONE_DAY : Long = 24 * 60 * 60 * 1000
    }
}


