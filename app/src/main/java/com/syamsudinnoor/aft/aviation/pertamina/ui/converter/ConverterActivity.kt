package com.syamsudinnoor.aft.aviation.pertamina.ui.converter

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityConverterBinding
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.ConversionUnit
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.FlowUnit
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.LengthUnit
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.MassFlow
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.PressureUnit
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.StandardizedFlow
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.VolumeUnit
import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis.VolumetricFlow

class ConverterActivity : AppCompatActivity() {
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var inputValue: EditText
    private lateinit var btnConvert: Button
    private lateinit var tvResult: TextView
    private lateinit var binding : ActivityConverterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Converter"

        spinnerCategory = binding.spinnerCategory
        spinnerFrom = binding.spinnerFrom
        spinnerTo = binding.spinnerTo
        inputValue = binding.inputValue
        btnConvert = binding.btnConvert
        tvResult = binding.tvResult

        val categories = listOf("Volumetric Flow Rate", "Mass Flow Rate", "Standardized Flow","Volume Unit","Length Unit","Pressure Unit")
        val adapterCategory = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapterCategory

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                when (categories[position]) {
                    "Volumetric Flow Rate" -> setUnitAdapter(VolumetricFlow.entries.toTypedArray())
                    "Mass Flow Rate" -> setUnitAdapter(MassFlow.entries.toTypedArray())
                    "Standardized Flow" -> setUnitAdapter(StandardizedFlow.entries.toTypedArray())
                    "Volume Unit" -> setUnitAdapter(VolumeUnit.entries.toTypedArray())
                    "Length Unit" -> setUnitAdapter(LengthUnit.entries.toTypedArray())
                    "Pressure Unit" -> setUnitAdapter(PressureUnit.entries.toTypedArray())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        btnConvert.setOnClickListener {
            convert()
        }
    }

    private fun <T> setUnitAdapter(units: Array<T>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
    }

    private fun convert() {
        val valueText = inputValue.text.toString()
        if (valueText.isEmpty()) {
            Toast.makeText(this, "Masukkan nilai dulu", Toast.LENGTH_SHORT).show()
            return
        }

        val value = valueText.toDouble()
        val fromUnit = spinnerFrom.selectedItem as ConversionUnit
        val toUnit = spinnerTo.selectedItem as ConversionUnit

        val baseValue = value * fromUnit.toBase
        val result = baseValue / toUnit.toBase

        tvResult.text = "$value ${fromUnit.label} = %.4f ${toUnit.label}".format(result)
    }
}