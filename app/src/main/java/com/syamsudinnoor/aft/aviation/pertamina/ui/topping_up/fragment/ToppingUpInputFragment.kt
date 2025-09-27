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
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.CardViewHolderBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentToppingUpInputBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.fragment.FragmentDippingRefeuller
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.DataToppingUpViewModel

import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumber
import com.syamsudinnoor.aft.aviation.pertamina.utility.helperSettingEditText
import java.sql.Time
import java.util.Date
import kotlin.getValue
import kotlin.math.min


class ToppingUpInputFragment : Fragment() {


    private lateinit var status : String
    private  var dataUpdate : ToppingUp? = null
    private var _binding : FragmentToppingUpInputBinding? = null
    private val binding get() = _binding!!

    private var statusRefeuller : String? = null

    private var statusLiterBefore : Double = 0.0
    private var statusLiterAfter : Double = 0.0

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

    private var totalisatorAkhir : String? = ""
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
        dataUpdate = arguments?.getParcelable<ToppingUp?>("UPDATE_DATA")
        if (dataUpdate != null){
            status = "UPDATE"

        }else{
            status = "INSERT"
        }

        if (status == "UPDATE"){
            setUpdateView()
            setupData(dataUpdate!!)
        }

        validateInput()
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

        viewModel.isValid.observe(viewLifecycleOwner) { isValid ->
            binding.buttonSearch.isEnabled = isValid
        }

        viewModel.isMM2Valid.observe(viewLifecycleOwner){isValid ->
            binding.buttonSearch2.isEnabled = isValid
        }


        binding.buttonSave.setOnClickListener {
            insertData()
        }

        return binding.root
    }


    private fun setUpdateView(){
        binding.buttonSearch.visibility = View.GONE
        binding.cardView1.cardViewResult.visibility = View.GONE
        binding.viewHide.visibility = View.VISIBLE
    }

    private fun setupData(toppingUp: ToppingUp){
        searchToppingUp()
        val ref  = toppingUp.sales_ref ?: 0
        binding.spinnerOperatorName.setText(toppingUp.operator)
        binding.spinnerRefeuller.setText(toppingUp.snr_no)
        binding.editTextMm.setText(toppingUp.sisa_dipping.toString())
        binding.editRef.setText(ref.toString())
        binding.editToppingVolume.setText(toppingUp.jumlah_topping.toString())
        binding.editTotalisatorAwal.setText(toppingUp.totalisator_awal)
        binding.editTotalisatorAkhir.setText(toppingUp.totalisator_akhir)
        binding.spinnerM.setText(toppingUp.m_number)
        binding.buttonStartTime.text = TimeConverter.toReadableTime(toppingUp.start_time?: System.currentTimeMillis())
        binding.buttonMinute.text = "${toppingUp.duration} Minute"
        binding.editDippingAfterTopping.setText(toppingUp.hasil_dipstik.toString())
        binding.spinnerTankQc.setText(toppingUp.tanki)
        binding.spinnerM.setText(toppingUp.m_number)
        binding.timeEnd.text = " = ${TimeConverter.toReadableTime(toppingUp.start_time!!)}"

        hourStart = toppingUp.start_time
        hourEnd = toppingUp.end_time
        duration = toppingUp.duration
        statusRefeuller = toppingUp.snr_no
        sisaDipping = toppingUp.sisa_dipping
        salesRef = toppingUp.sales_ref
        jumlahTopping = toppingUp.jumlah_topping
        hasilDipstik = toppingUp.hasil_dipstik
        mVariabel = toppingUp.m_number
        totalisatorAwal = toppingUp.totalisator_awal
        totalisatorAkhir = toppingUp.totalisator_akhir
        tangki = toppingUp.tanki
        operator = toppingUp.operator

        searchForToppingUp(hasilDipstik ?: 0.0,"after",binding.cardView2)

        binding.buttonSearch2.isEnabled = true


    }



    private fun insertData(){
        tangki = binding.spinnerTankQc.text.toString()
        operator = binding.spinnerOperatorName.text.toString()
        salesRef = binding.editRef.text.toString().toIntOrNull()
        jumlahTopping = binding.editToppingVolume.text.toString().toIntOrNull()
        totalisatorAwal = binding.editTotalisatorAwal.text.toString()
        totalisatorAkhir = binding.editTotalisatorAkhir.text.toString()
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
            operator = operator,
            totalisator_akhir = totalisatorAkhir
            )

        val dataToUpdate = ToppingUp(
            id = dataUpdate?.id!!,
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
            operator = operator,
            totalisator_akhir = totalisatorAkhir
        )

        if (status == "INSERT"){
            toppingUpViewModel.insert(data)
            Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
        }else if (status == "UPDATE"){
            toppingUpViewModel.updateToppingUp(dataToUpdate)
            Toast.makeText(requireContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
        }
        binding.buttonSave.isEnabled = false
    }


    private fun pickupTime(context: Context){
        val dialogView = layoutInflater.inflate(R.layout.layout_picker_duration,null)
        val hourPicker  = dialogView.findViewById<NumberPicker>(com.syamsudinnoor.aft.aviation.pertamina.R.id.hourPicker)
        val minutePicker = dialogView.findViewById<NumberPicker>(com.syamsudinnoor.aft.aviation.pertamina.R.id.minutePicker)

        hourPicker.minValue = 0
        hourPicker.maxValue = 2

        minutePicker.minValue = 0
        minutePicker.maxValue = 60

        AlertDialog.Builder(context)
            .setTitle("Total Waktu Pengisian")
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

            val cardView = binding.cardView1
            cardView.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)

            val value = binding.editTextMm.text.toString().toDouble()
            sisaDipping = value.toInt()

            val statusKirim = "before"
            searchForToppingUp(value,statusKirim,cardView)
            binding.viewHide.visibility = View.VISIBLE
        }
        binding.buttonSearch2.setOnClickListener {
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.editDippingAfterTopping.windowToken, 0)
            val cardView = binding.cardView2
            cardView.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
            val value = binding.editDippingAfterTopping.text.toString().toDouble()
            hasilDipstik = value
            val statusKirim = "after"

            searchForToppingUp(value,statusKirim,cardView)
        }
    }

    private fun searchForToppingUp(value : Double ,status: String,cardView: CardViewHolderBinding){
        when(statusRefeuller){
            "SNR 17" -> {
                if (status == "before"){
                    statusLiterBefore = binding.editTextMm.text.toString().toDouble()
                    sisaDipping = statusLiterBefore.toInt()


                }else{
                    statusLiterAfter = binding.editDippingAfterTopping.text.toString().toDouble()
                    hasilDipstik = statusLiterAfter
                }

            }
            "SNR 19" -> {
                if (status == "before"){
                    statusLiterBefore = binding.editTextMm.text.toString().toDouble()
                    sisaDipping = statusLiterBefore.toInt()

                }else{
                    statusLiterAfter = binding.editDippingAfterTopping.text.toString().toDouble()
                    hasilDipstik = statusLiterAfter
                }
            }
            "SNR 20" -> when{status == "before" -> viewModel.getSnr20(value) else -> viewModel.getSnr20After(value)}
            "SNR 21" -> when{status == "before" -> viewModel.getSnr21(value) else -> viewModel.getSnr21After(value)}
            "SNR 22" -> when{status == "before" -> viewModel.getSnr22(value) else -> viewModel.getSnr22After(value)}
            else -> {}
        }
        if (status == "before"){
            setupToppingUp(cardView)
            statusToppingUp(cardView,status)
            binding.viewHide.visibility = View.VISIBLE
        }
        else{
            setupToppingUpAfter(cardView)
            statusToppingUp(cardView,status)
            binding.buttonSave.isEnabled = true
        }

    }

    
    fun validateInput() {

        binding.spinnerRefeuller.addTextChangedListener(helperSettingEditText { s ->
            if (s.isNotEmpty()) {
                statusRefeuller = s
                viewModel.onRefeullerSelected(true)
                if (s == "SNR 17" || s == "SNR 19") {
                    binding.containterMM.hint = getString(R.string.inset_liter)
                } else {
                    binding.containterMM.hint = getString(R.string.insert_mm)
                }
            } else {
                viewModel.onRefeullerSelected(false)
            }
        })

        binding.editTextMm.addTextChangedListener(helperSettingEditText { s ->
            if (s.isNotEmpty()){
                viewModel.onMMValid(true)
                viewModel.onSessionSelected(true)
            }else{
                viewModel.onMMValid(false)
                viewModel.onSessionSelected(false)
            }
        })

        binding.editDippingAfterTopping.addTextChangedListener(helperSettingEditText { s ->
            if (s.isNotEmpty()){
                viewModel.onMM2Valid(true)
            }else{
                viewModel.onMM2Valid(false)
            }
        })

    }

    private fun setupToppingUp(cardView : CardViewHolderBinding) {

        viewModel.snr20.observe(viewLifecycleOwner){ liter ->
            if(liter != null){
                statusLiterBefore = liter.liter!!
                //sisaDipping = statusLiterBefore.toInt()
                statusToppingUp(cardView)
            }else{
                cardView.imgResultIcon.visibility = View.VISIBLE
                cardView.tableViewResult.visibility = View.GONE
                cardView.imgResultIcon.setImageResource(R.drawable.not_found)
            }

        }

        viewModel.snr21.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                statusLiterBefore = liter.liter!!
                //sisaDipping = statusLiterBefore.toInt()
                statusToppingUp(cardView)
            }else{
                cardView.imgResultIcon.visibility = View.VISIBLE
                cardView.tableViewResult.visibility = View.GONE
                cardView.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

        viewModel.snr22.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                statusLiterBefore = liter.liter!!
               // sisaDipping = statusLiterBefore.toInt()
                statusToppingUp(cardView)
            }else{
                cardView.imgResultIcon.visibility = View.VISIBLE
                cardView.tableViewResult.visibility = View.GONE
                cardView.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

    }

    private fun setupToppingUpAfter(cardView : CardViewHolderBinding) {

        viewModel.snr20After.observe(viewLifecycleOwner){ liter ->
            if(liter != null){
                statusLiterAfter = liter.liter!!
                //hasilDipstik = statusLiterAfter
                statusToppingUp(cardView,"after")
            }else{
                cardView.imgResultIcon.visibility = View.VISIBLE
                cardView.tableViewResult.visibility = View.GONE
                cardView.imgResultIcon.setImageResource(R.drawable.not_found)
            }

        }

        viewModel.snr21After.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                statusLiterAfter = liter.liter!!
                //hasilDipstik = statusLiterAfter
                statusToppingUp(cardView,"after")
            }else{
                cardView.imgResultIcon.visibility = View.VISIBLE
                cardView.tableViewResult.visibility = View.GONE
                cardView.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

        viewModel.snr22After.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                statusLiterAfter = liter.liter!!
                //hasilDipstik = statusLiterAfter
                statusToppingUp(cardView,"after")
            }else{
                cardView.imgResultIcon.visibility = View.VISIBLE
                cardView.tableViewResult.visibility = View.GONE
                cardView.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

    }

    private fun statusToppingUp(cardView : CardViewHolderBinding,status : String = "before"){
        cardView.imgResultIcon.visibility = View.GONE
        cardView.tableViewResult.visibility = View.VISIBLE

        val maxCaps = when{
            statusRefeuller == "SNR 17" || statusRefeuller == "SNR 21" || statusRefeuller == "SNR 22" -> CONST_17_21_22
            else -> CONST_19_20
        }

        val usesLiter = when{ status == "before" -> statusLiterBefore else -> statusLiterAfter}

        val ullage : Double = maxCaps - usesLiter


        val decision = (usesLiter / maxCaps) * 100
        val textDecision = when{
            decision >= 50 ->"Belum butuh Topping Up"
            decision >= 25 -> "Akan butuh Topping Up"
            else -> "Secepatnya Topping Up"
        }

        cardView.row1Header.text = resources.getString(R.string.dipping_result_tank)
        cardView.row1Body.text = getString(R.string.liter_holder, numberFormatter(usesLiter))
        cardView.row2Header.text = getString(R.string.max_capacity_tank)
        cardView.row2Body.text = getString(R.string.liter_holder, numberFormatter(maxCaps))


        if (status == "before"){
            cardView.row3Header.text = resources.getString(R.string.decision_topping_up)
            cardView.row3Body.text = textDecision
            cardView.row4Header.text = getString(R.string.recomendation)
        }else{
            cardView.row3Header.visibility = View.GONE
            cardView.row3Body.visibility = View.GONE
            cardView.row4Header.text = getString(R.string.ullage_tank)
        }
        cardView.row4Body.text = formatterNumber(ullage)


        when{
            decision >= 50 -> cardView.cardViewResult.setBackgroundResource(R.drawable.green_card_background)

            decision >= 25 -> cardView.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)

            else -> cardView.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
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

        fun newInstance(toppings : ToppingUp?) = ToppingUpInputFragment().apply {
            arguments = Bundle().apply {
                putParcelable("UPDATE_DATA",toppings)
            }
        }

        const val APP_NAME = "APP_NAME"
        private const val CONST_17_21_22 = 24500.0
        private const val CONST_19_20 = 25000.0

    }

}


