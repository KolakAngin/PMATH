package com.syamsudinnoor.aft.aviation.pertamina.ui.aircraft

import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityAircraftBinding
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter

class AircraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAircraftBinding
    private lateinit var viewModel: AircraftViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityAircraftBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this )[AircraftViewModel::class.java]
        //basic setting actionbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = intent.getStringExtra(APP_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        viewModel.isValid.observe(this) { isValid ->
            binding.buttonCalculate.isEnabled = isValid
        }

        validateInput()
        binding.buttonCalculate.setOnClickListener {
            binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
            calculateAircraft()
        }
    }

    private fun calculateAircraft() {
        binding.textViewResult.visibility = View.VISIBLE
        binding.imgResultIcon.visibility = View.GONE


        val finalRequest = binding.finalRequest.text.toString().toDouble()
        val remain = binding.remain.text.toString().toDouble()
        val remainRefeuller = binding.refeullerVolume.text.toString().toDouble()

        val result = (finalRequest - remain) / CONSTANTA
        val decision = remainRefeuller - result

        val isOke = getString(
            R.string.oke_stock_aircraft,
            numberFormatter(result),
            numberFormatter(remainRefeuller)
        )

        val isOkeWithWarning = getString(
            R.string.oke_stock_aircraft_warning,
            numberFormatter(result),
            numberFormatter(remainRefeuller)
        )
        val isNotOke = getString(
            R.string.not_oke_stock_aircraft,
            numberFormatter(result),
            numberFormatter(remainRefeuller)
        )


        when{
            decision > 1000 ->{
                binding.textViewResult.text = isOke
                binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
            }
            decision <= 1000  && decision >= 0 ->{
                binding.textViewResult.text = isOkeWithWarning
                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
            }
            decision <= 0 -> {
                binding.textViewResult.text = isNotOke
                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)

            }

        }


    }


    private fun validateInput(){
        binding.finalRequest.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if(s.toString().isNotEmpty()){

                    viewModel.onFinalInputChanged(true)
                }else{
                    viewModel.onFinalInputChanged(false)

                }
            }

        })

        binding.remain.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if(s.toString().isNotEmpty()){
                    viewModel.onRemainInputChanged(true)
                }else{
                    viewModel.onRemainInputChanged(false)

                }
            }

        })


        binding.refeullerVolume.addTextChangedListener(object : TextWatcher {
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
                if (s.toString().isNotEmpty()) {
                    viewModel.onRemainRefeullerChanged(true)
                } else {
                    viewModel.onRemainRefeullerChanged(false)
                }
            }

        })

    }


    companion object{
        const val APP_NAME = "Aircraft"
        private val CONSTANTA = 0.798

    }
}
