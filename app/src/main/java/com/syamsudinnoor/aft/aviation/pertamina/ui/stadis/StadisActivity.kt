package com.syamsudinnoor.aft.aviation.pertamina.ui.stadis

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityStadisBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.CostumInformationBinding
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumber
import com.syamsudinnoor.aft.aviation.pertamina.utility.textWatcherWithNumber

class StadisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStadisBinding

    private var volumeSource : Int? = null
    private var densitySource : Double? = null
    private var temperatureSource : Double? = null
    private var electricalSource : Int? = null

    private var volumeDestination : Int? = null
    private var densityDestination : Double? = null
    private var temperatureDestination : Double? = null
    private var electricalDestination : Int? = null

    private var blendResult : Double = 0.0
    private var litersEstimationStadis  : Double = 0.0
    private var volumeStadis2 : Double = 0.0
    private var diffCU : Double = 0.0
    private var concentrationStadis : Double = 0.0
    private var targetCU : Int = 0
    private var avturAsMixer : Double = 0.0
    private var stadisPlusAvturAsMixer : Double = 0.0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStadisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        setSupportActionBar(binding.topAppBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.editSourceVolumeLiter.apply { addTextChangedListener(textWatcherWithNumber(this@apply)) }
        binding.editDestinationVolumeLiter.apply { addTextChangedListener(textWatcherWithNumber(this@apply)) }
        binding.editVolumeLiterStadis.apply { addTextChangedListener(textWatcherWithNumber(this@apply)) }

        settingPopUp()

        binding.buttonHitung.setOnClickListener {
            calculateDoppingStadis()
        }

        binding.buttonHitungCU.setOnClickListener {
            showCalcRes()
        }

    }


    private fun calculateDoppingStadis(){
        volumeSource = binding.editSourceVolumeLiter.text.toString().replace(".","").toIntOrNull()
        densitySource = binding.editSourceDensityObserved.text.toString().toDoubleOrNull()
        temperatureSource = binding.editSourceTemperature.text.toString().toDoubleOrNull()
        electricalSource = binding.editSourceElectricalConductivity.text.toString().toIntOrNull()

        volumeDestination = binding.editDestinationVolumeLiter.text.toString().replace(".","").toIntOrNull()
        densityDestination = binding.editDestinationDensityObserved.text.toString().toDoubleOrNull()
        temperatureDestination = binding.editDestinationTemperature.text.toString().toDoubleOrNull()
        electricalDestination = binding.editDestinationElectricalConductivity.text.toString().toIntOrNull()


        if (volumeSource != null && densitySource != null && temperatureSource != null && electricalSource != null &&
            volumeDestination != null && densityDestination != null && temperatureDestination != null && electricalDestination != null)
        {
            showStatusCU()
        }else Toast.makeText(this,"Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()

    }

    private fun showStatusCU() {
        binding.cardViewStatusDopping.imgResultIcon.visibility = View.GONE
        binding.cardViewStatusDopping.tableViewResult.visibility = View.VISIBLE


        binding.cardViewStatusDopping.row1Header.visibility = View.VISIBLE
        binding.cardViewStatusDopping.row1Body.visibility = View.VISIBLE
        binding.cardViewStatusDopping.row2Header.visibility = View.VISIBLE
        binding.cardViewStatusDopping.row2Body.visibility = View.VISIBLE

        binding.cardViewStatusDopping.row3Body.visibility = View.GONE
        binding.cardViewStatusDopping.row3Header.visibility = View.GONE
        binding.cardViewStatusDopping.row4Body.visibility = View.GONE
        binding.cardViewStatusDopping.row4Header.visibility = View.GONE

        blendResult = (((volumeSource!! * electricalSource!!) + (volumeDestination!! * electricalDestination!!)) / ((volumeDestination!! + volumeSource!!))).toDouble()

        if (blendResult in 80.0..800.0){
            binding.cardViewStatusDopping.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
            binding.cardViewStatusDopping.row1Header.text = "Blend result"
            binding.cardViewStatusDopping.row1Body.text = blendResult.toString()
            binding.cardViewStatusDopping.row2Header.text = "Status"
            binding.cardViewStatusDopping.row2Body.text = "OK"

            binding.txtTargetCu.visibility = View.GONE
            binding.editTargetCu.visibility = View.GONE
            binding.buttonHitungCU.visibility = View.GONE
            binding.cardViewKebutuhanDopping.cardViewResult.visibility = View.GONE

            binding.editVolumeLiterStadis.visibility = View.GONE
            binding.editTargetCu.visibility = View.GONE
            binding.cardViewResultKebutuhanDopping.cardViewResult.visibility = View.GONE




        }else{
            binding.cardViewStatusDopping.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
            binding.cardViewStatusDopping.row1Header.text = "Blend result"
            binding.cardViewStatusDopping.row1Body.text = blendResult.toInt().toString()
            binding.cardViewStatusDopping.row2Header.text = "Status"
            binding.cardViewStatusDopping.row2Body.text = "NOT OK"

            binding.txtTargetCu.visibility = View.VISIBLE
            binding.editTargetCu.visibility = View.VISIBLE
            binding.editVolumeLiterStadis.visibility = View.VISIBLE
            binding.buttonHitungCU.visibility = View.VISIBLE
            binding.cardViewKebutuhanDopping.cardViewResult.visibility = View.VISIBLE

            binding.editVolumeLiterStadis.setText(volumeSource.toString())
        }


    }

    private fun showCalcRes(){
        val literX = binding.editVolumeLiterStadis.text.toString().replace(".","").toDoubleOrNull()
        val targetX = binding.editTargetCu.text.toString().toIntOrNull()

        if (literX != null && targetX != null){
            litersEstimationStadis = literX
            targetCU = targetX
        }else{
            Toast.makeText(this,"Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        diffCU = targetCU - blendResult
        concentrationStadis = if(diffCU < 0.0) 0.0 else diffCU / 200.0
        volumeStadis2 = (litersEstimationStadis * concentrationStadis) / 1000.0
        avturAsMixer = litersEstimationStadis * 0.02 * concentrationStadis
        stadisPlusAvturAsMixer = avturAsMixer + volumeStadis2

        binding.cardViewKebutuhanDopping.cardViewResult.visibility = View.VISIBLE
        binding.cardViewKebutuhanDopping.imgResultIcon.visibility = View.GONE
        binding.cardViewKebutuhanDopping.tableViewResult.visibility = View.VISIBLE


        binding.cardViewKebutuhanDopping.row1Header.text = "Selisih"
        binding.cardViewKebutuhanDopping.row1Body.text = diffCU.toInt().toString()
        binding.cardViewKebutuhanDopping.row2Header.text = "Vol STADIS 2"
        binding.cardViewKebutuhanDopping.row2Body.text = volumeStadis2.toString() + " CC"
        binding.cardViewKebutuhanDopping.row3Header.text = "Kons. STADIS 1"
        binding.cardViewKebutuhanDopping.row3Body.text = concentrationStadis.toString() + " mg/L"
        binding.cardViewKebutuhanDopping.row4Header.visibility = View.GONE
        binding.cardViewKebutuhanDopping.row4Body.visibility = View.GONE

        showBlendRes()

    }

    private fun showBlendRes(){
        binding.cardViewResultKebutuhanDopping.cardViewResult.visibility = View.VISIBLE
        binding.cardViewResultKebutuhanDopping.tableViewResult.visibility = View.VISIBLE
        binding.cardViewResultKebutuhanDopping.imgResultIcon.visibility = View.GONE


        binding.cardViewResultKebutuhanDopping.row1Header.text = "Vol. Avtur"
        binding.cardViewResultKebutuhanDopping.row1Body.text = formatterNumber(litersEstimationStadis)

        binding.cardViewResultKebutuhanDopping.row2Header.text = "Stadis-450"
        binding.cardViewResultKebutuhanDopping.row2Body.text = volumeStadis2.toInt().toString() + " CC"

        binding.cardViewResultKebutuhanDopping.row3Header.text = "Avtur as Mixer"
        binding.cardViewResultKebutuhanDopping.row3Body.text = formatterNumber(avturAsMixer)

        binding.cardViewResultKebutuhanDopping.row4Header.text = "Stadis-450 + Mixer"
        binding.cardViewResultKebutuhanDopping.row4Body.text = formatterNumber(stadisPlusAvturAsMixer)


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when{
            item.itemId == android.R.id.home ->{
                finish()
                true
            }else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun settingPopUp(){
        val dialog = Dialog(this)
        val dialogBinding = CostumInformationBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()

        dialogBinding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }

    }
}