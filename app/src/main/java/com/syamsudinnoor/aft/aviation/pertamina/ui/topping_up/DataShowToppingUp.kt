package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityDataShowToppingUpBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder.BridgerDataShowActivty
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.ToppingUpActivity.Companion.APP_NAME
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumber

class DataShowToppingUp : AppCompatActivity() {

    private lateinit var binding : ActivityDataShowToppingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDataShowToppingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reportData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(BridgerDataShowActivty.Companion.APP_NAME, ToppingUp::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(APP_NAME)
        }


        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = APP_NAME
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupData(reportData!!)
    }

    private fun setupData(data : ToppingUp) {
        binding.row1Body.text =
            TimeConverter.toReadableDate(data.start_time ?: System.currentTimeMillis())
        binding.row2Body.text =
            TimeConverter.toReadableTime(data.start_time ?: System.currentTimeMillis())
        binding.row3Body.text =
            TimeConverter.toReadableTime(data.end_time ?: System.currentTimeMillis())
        binding.row4Body.text = data.duration?.toString() + " Menit"
        binding.row5Body.text = data.snr_no
        binding.row6Body.text = formatterNumber(data.sisa_dipping)
        binding.row7Body.text = formatterNumber(data.sales_ref)
        binding.row8Body.text = formatterNumber(data.jumlah_topping)
        binding.row9Body.text = formatterNumber(data.hasil_dipstik)
        binding.row10Body.text = data.m_number
        binding.row11Body.text = data.totalisator_awal
        binding.row12Body.text = data.totalisator_akhir
        binding.row13Body.text = data.tanki
        binding.row14Body.text = data.operator
    }

    companion object{
        const val APP_NAME = "Detail Data"
    }


}