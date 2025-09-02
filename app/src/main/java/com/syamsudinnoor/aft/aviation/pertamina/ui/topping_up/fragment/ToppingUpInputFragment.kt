package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentToppingUpInputBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.DataToppingUpViewModel

import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import java.sql.Time
import java.util.Date
import kotlin.getValue
import kotlin.math.min


class ToppingUpInputFragment : Fragment() {

    private var _binding : FragmentToppingUpInputBinding? = null
    private val binding get() = _binding!!

    private var statusRefeuller : String? = null
    private var statusSession : String? = null

    private var statusLiter : Double = 0.0
    private val viewModel: ToppingUpViewModel by viewModels {
        val database = SnoorRoomDatabase.getDatabase(requireContext())
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }

    private val toppingUpViewModel : DataToppingUpViewModel by viewModels {
        val database = MainDatabase.getDatabase(requireContext())
        val repository = MainRepository(database.getDao())
        MainViewModelFactory(repository)
    }

    private var hourStart : Long? = null
    private var hourEnd : Long? = null
    private var duration : Int? = 0
    private var salesRef : Int? = 0
    private var sisaDipping : Int? = 0
    private var jumlahTopping : Int? = 0
    private var hasilDipstik : Double? = 0.0
    private var mVariabel : String? = ""
    private var totalisatorAwal : String? = ""
    private var tangki : String? = ""
    private var operator : String? = ""

    private var minuteStart : Long? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentToppingUpInputBinding.inflate(inflater, container, false)



        validateInput()
        setupToppingUp()
        searchToppingUp()

        binding.buttonStartTime.setOnClickListener {
            val date = Date()
            timeDialog(requireContext(),"Start Time",{ hour, minute ->
                hourStart = TimeConverter.concatenateStringDate(date,hour.toString(),minute.toString())
                binding.buttonStartTime.text = TimeConverter.toReadableTime(hourStart!!)
            })

        }

        binding.buttonMinute.setOnClickListener {
            pickupTime(requireContext())
        }

        viewModel.isValid.observe(viewLifecycleOwner){ isValid ->
            binding.buttonSearch.isEnabled = isValid
        }

        binding.buttonSave.setOnClickListener {
            insertData()
        }

        return binding.root
    }

    private fun insertData(){
        tangki = binding.spinnerTankQc.text.toString()
        operator = binding.spinnerOperatorName.text.toString()
        salesRef = binding.editRef.text.toString().toIntOrNull()
        jumlahTopping = binding.editToppingVolume.text.toString().toIntOrNull()
        totalisatorAwal = binding.editTotalisator.text.toString()
        mVariabel = binding.spinnerM.text.toString()


        val data = ToppingUp(
            start_time = hourStart,
            end_time = hourEnd,
            duration = duration,
            snr_no = statusRefeuller ?: "",
            sisa_dipping = sisaDipping,
            sales_ref = salesRef,
            jumlah_topping = jumlahTopping,
            hasil_dipstik = hasilDipstik,
            m_number = mVariabel,
            totalisator_awal = totalisatorAwal,
            tanki = tangki,
            operator = operator
            )

        toppingUpViewModel.insert(data)
        Toast.makeText(requireContext(),"Data berhasil disimpan",Toast.LENGTH_SHORT).show()
        binding.buttonSave.isEnabled = false
    }


    private fun pickupTime(context: Context){
        val dialogView = layoutInflater.inflate(R.layout.layout_picker_duration,null)
        val hourPicker  = dialogView.findViewById<NumberPicker>(com.syamsudinnoor.aft.aviation.pertamina.R.id.hourPicker)
        val minutePicker = dialogView.findViewById<NumberPicker>(com.syamsudinnoor.aft.aviation.pertamina.R.id.minutePicker)

        hourPicker.minValue = 0
        hourPicker.maxValue = 23

        minutePicker.minValue = 0
        minutePicker.maxValue = 60

        AlertDialog.Builder(context)
            .setTitle("Pilih Waktu")
            .setView(dialogView)
            .setPositiveButton("OK"){_,_ ->

                val result = when{
                    hourPicker.value == 0 -> minutePicker.value * 60 * 1000L
                    else -> hourPicker.value * 60 * 60 * 1000L + minutePicker.value * 60 * 1000L
                }

                val resultShow = when{
                    hourPicker.value == 0 -> minutePicker.value
                    else -> hourPicker.value * 60  + minutePicker.value
                }
                minuteStart = result
                duration = resultShow
                if (hourStart != null){
                    hourEnd = hourStart!! + minuteStart!!
                    binding.timeEnd.text = " = ${TimeConverter.toReadableTime(hourEnd!!)}"
                    binding.buttonMinute.text = "${resultShow} Minute"
                }

            }
            .setNegativeButton("Cancel",null)
            .show()

    }

    private fun searchToppingUp() {
        binding.buttonSearch.setOnClickListener {

            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.editTextMm.windowToken, 0)
            binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
            val value = binding.editTextMm.text.toString().toDouble()
            sisaDipping = value.toInt()
            when(statusRefeuller){
                "SNR 17" -> {
                    statusLiter = binding.editTextMm.text.toString().toDouble()
                    hasilDipstik = statusLiter
                    simplifySetupToppingUp(this.statusSession)
                }
                "SNR 19" -> {
                    hasilDipstik = statusLiter
                    statusLiter = binding.editTextMm.text.toString().toDouble()
                    simplifySetupToppingUp(this.statusSession)
                }
                "SNR 20" -> viewModel.getSnr20(value)
                "SNR 21" -> viewModel.getSnr21(value)
                "SNR 22" -> viewModel.getSnr22(value)
                else -> {}
            }
            binding.buttonSave.isEnabled = true
        }
    }




    fun validateInput() {

        binding.spinnerRefeuller.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val watching = s.toString()
                if (watching.isNotEmpty()) {
                    statusRefeuller = watching
                    viewModel.onRefeullerSelected(true)
                    if (watching == "SNR 17" || watching == "SNR 19") {
                        binding.containterMM.hint = getString(R.string.inset_liter)
                    } else {
                        binding.containterMM.hint = getString(R.string.insert_mm)
                    }
                } else {
                    viewModel.onRefeullerSelected(false)
                }
            }
        })

        binding.rgSession.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_topping_up ->{
                    this.statusSession = "Topping Up"
                    viewModel.onSessionSelected(true)
                }
                R.id.rb_settle_refeuller ->{
                    this.statusSession = "Settle"
                    viewModel.onSessionSelected(true)
                }
                else -> {
                    viewModel.onSessionSelected(false)
                }
            }
        }

                binding.editTextMm.addTextChangedListener(object : TextWatcher{
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
                if (s.toString().isNotEmpty()){
                    viewModel.onMMValid(true)
                }else{
                    viewModel.onMMValid(false)
                }
            }

        })

    }

    private fun setupToppingUp() {

        viewModel.snr20.observe(viewLifecycleOwner){ liter ->
            if(liter != null){
                this.statusLiter = liter.liter ?: 0.0
                hasilDipstik = this.statusLiter
                simplifySetupToppingUp(this.statusSession)
            }else{
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
            }

        }

        viewModel.snr21.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                this.statusLiter = liter.liter ?: 0.0
                hasilDipstik = this.statusLiter
                simplifySetupToppingUp(this.statusSession)
            }else{
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

        viewModel.snr22.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                this.statusLiter = liter.liter ?: 0.0
                hasilDipstik = this.statusLiter
                simplifySetupToppingUp(this.statusSession)
            }else{
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

    }

    private fun simplifySetupToppingUp(status: String?){
        when(status){
            "Topping Up" -> statusToppingUp()
            "Settle" -> statusSettle()
        }
    }

    private fun statusToppingUp(){
        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE


        val decision = (statusLiter / CONSTANTA) * 100
        val textDecision = when{
            decision >= 50 ->"Belum butuh Topping Up"
            decision >= 25 -> "Akan butuh Topping Up"
            else -> "Secepatnya Topping Up"
        }

        binding.row1Header.text = resources.getString(R.string.dipping_result_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(statusLiter))
        binding.row2Header.text = getString(R.string.max_capacity_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(CONSTANTA))
        binding.row3Header.text = resources.getString(R.string.decision_topping_up)
        binding.row3Body.text = textDecision
        binding.row4Header.visibility = View.GONE
        binding.row4Body.visibility = View.GONE


        when{
            decision >= 50 -> binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)

            decision >= 25 -> binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)

            else -> binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
        }

    }

    private fun statusSettle(){
        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE


        val ullage = CONSTANTA - statusLiter
        val ullagePercent = (ullage / CONSTANTA) * 100


        binding.row1Header.text = resources.getString(R.string.dipping_result_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(statusLiter))

        binding.row2Header.text = resources.getString(R.string.max_capacity_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(CONSTANTA))

        binding.row3Header.text = resources.getString(R.string.ullage_tank)
        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(ullage))

        binding.row4Header.visibility = View.GONE
        binding.row4Body.visibility = View.GONE


        when{
            ullagePercent >= 75 -> binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)

            ullagePercent >= 50 -> binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)

            else -> binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
        }

    }

        override fun onResume() {
            super.onResume()
            val refeullers = resources.getStringArray(R.array.refeullers_choice)
            val operators = resources.getStringArray(R.array.operator_choice)
            val mChoice = resources.getStringArray(R.array.m_choice)
            val tanks = resources.getStringArray(R.array.tangks_choice)

            val adapterOperators = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, operators)
            val adapterRefeulers = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, refeullers)
            val adapterM = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, mChoice)
            val adapterTanks = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, tanks)

            binding.spinnerTankQc.setAdapter(adapterTanks)
            binding.spinnerOperatorName.setAdapter(adapterOperators)
            binding.spinnerRefeuller.setAdapter(adapterRefeulers)
            binding.spinnerM.setAdapter(adapterM)
    }



    companion object {
        private const val CONSTANTA = 25000.0
    }



}


