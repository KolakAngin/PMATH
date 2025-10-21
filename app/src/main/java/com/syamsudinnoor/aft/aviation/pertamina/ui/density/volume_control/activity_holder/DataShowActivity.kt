package com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.activity_holder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.adapter.DetailKompartemenAdapter
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityDataShowBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder.BridgerDataShowActivty.Companion.APP_NAME
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumber

class DataShowActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDataShowBinding
    private var updateData : AnalisaVolumeControlWithDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDataShowBinding.inflate(layoutInflater)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))


        setContentView(binding.root)
        updateData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("DATA", AnalisaVolumeControlWithDetail::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("DATA")
        }

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Show Data"

        setupHeader(updateData!!)
        setupAdapter(updateData!!)

    }

    private fun setupAdapter(data: AnalisaVolumeControlWithDetail) {
        binding.rvDataKompartemen.adapter = DetailKompartemenAdapter(data.detailKompartemen.filter { it.kompartemen != null})
        binding.rvDataKompartemen.layoutManager = LinearLayoutManager(this)
    }

    private fun setupHeader(data: AnalisaVolumeControlWithDetail) {

        binding.row1Body.text = TimeConverter.toReadableDateTime(data.analisaVolumeControl.tanggal ?: System.currentTimeMillis())
        binding.row2Body.text = data.analisaVolumeControl.aft ?: ""
        binding.row3Body.text = data.analisaVolumeControl.supply_point ?: ""
        binding.row4Body.text = data.analisaVolumeControl.transportir ?: ""
        binding.row5Body.text = data.analisaVolumeControl.no_polisi ?: ""
        binding.row6Body.text = formatterNumber(data.analisaVolumeControl.kuantitas)
        binding.row7Body.text = formatterNumber(data.analisaVolumeControl.harga_avtur)
        binding.row8Body.text = data.analisaVolumeControl.spv_rsd ?: ""
        binding.row9Body.text = data.analisaVolumeControl.sopir_bridger_1 ?: ""
        binding.row10Body.text = data.analisaVolumeControl.sopir_bridger_2 ?: ""
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.update_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.update_data ->{
                val intent = Intent(this, VolumeControlEditDataActivity::class.java)
                intent.putExtra("UPDATE_DATA", updateData)
                startActivity(intent)
                finish()
                true

            }
            android.R.id.home ->{
                finish()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }
}