package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityBridgerDataShowActivtyBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter

class BridgerDataShowActivty : AppCompatActivity() {

    private var _binding : ActivityBridgerDataShowActivtyBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        _binding = ActivityBridgerDataShowActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Show Data"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val reportData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(APP_NAME, BridgerQualityControl::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(APP_NAME)

        }

        setupData(reportData!!)

    }

    private fun setupData(data : BridgerQualityControl){
        binding.row1Body.text = TimeConverter.toReadableDateTime(data.dateTime ?: System.currentTimeMillis())
        binding.row2Body.text = data.bridger_no
        binding.row3Body.text = data.bpp
        binding.row5Body.text = data.volume_liter?.toString()
        binding.row6Body.text = data.seal
        binding.row7Body.text = data.test_report_no
        binding.row8Body.text = data.density_15_from_distributor?.toString()
        binding.row9Body.text = data.afrn_no
        binding.row10Body.text = data.density_obsd_rec_document?.toString()
        binding.row12Body.text = data.temp_rec_document?.toString()
        binding.row14Body.text = data.density_15_result_rec_document?.toString()
        binding.row15Body.text = data.density_obsd?.toString()
        binding.row16Body.text = data.temprature?.toString()
        binding.row17Body.text = data.density_15_calculation?.toString()
        binding.row18Body.text = data.app_star
        binding.row19Body.text = data.cu_psm?.toString()
        binding.row20Body.text = data.tangki
        binding.row21Body.text = data.operator_name
        binding.row24Body.text = data.diff_from_density_distributor?.toString()

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val APP_NAME = "Quality Control"
    }
}