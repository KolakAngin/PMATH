package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentKonsinyasiBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.DataToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.textWatcherWithNumber
import java.util.Date


class KonsinyasiFragment : Fragment() {

    private var _binding : FragmentKonsinyasiBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel : DataToppingUpViewModel by viewModels {
        val database = MainDatabase.getDatabase(requireContext())
        val repository = MainRepository(database.getDao())
        MainViewModelFactory(repository)
    }

    private var hourStart : Long? = null
    private var hourEnd : Long? = null
    private var duration : Int? = 0
    private var minuteStart : Long? = null


    private var operator : String? = null
    private var noBridger : String? = null
    private var totalLiter : Int? = null
    private var firstTotalisator : String? = null
    private var lastTotalisator : String? = null
    private var mVariabel : String? = null
    private var note : String? = null
    private var dippingAfterTopping : Double? = null
    private var fromTank : String? = null

    private var dataUpdate : ToppingUp? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKonsinyasiBinding.inflate(inflater, container, false)

        dataUpdate = arguments?.getParcelable<ToppingUp?>("UPDATE_DATA")

        if (dataUpdate != null){
            setupData()
        }

        binding.editToppingVolume.apply {
            addTextChangedListener(textWatcherWithNumber(binding.editToppingVolume))
        }

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

        binding.buttonSave.setOnClickListener {
            insertData()
        }



        return binding.root
    }


    private fun setupData(){
        binding.spinnerOperatorName.setText(dataUpdate?.operator ?: "")
        binding.editNomorBridger.setText(dataUpdate?.snr_no ?: "")
        binding.editToppingVolume.setText(dataUpdate?.jumlah_topping.toString())
        binding.editTotalisatorAwal.setText(dataUpdate?.totalisator_awal.toString())
        binding.editTotalisatorAkhir.setText(dataUpdate?.totalisator_akhir.toString())
        binding.spinnerM.setText(dataUpdate?.m_number ?: "")
        binding.buttonStartTime.text = TimeConverter.toReadableTime(dataUpdate?.start_time?: System.currentTimeMillis())
        binding.buttonMinute.text = "${dataUpdate?.duration ?: ""} Minute"
        binding.editDippingAfterTopping.setText(dataUpdate?.hasil_dipstik?.toInt()?.toString() ?: "")
        binding.spinnerTankQc.setText(dataUpdate?.tanki ?: "")
        binding.spinnerM.setText(dataUpdate?.m_number ?: "")
        binding.timeEnd.text = " = ${TimeConverter.toReadableTime(dataUpdate?.start_time ?: System.currentTimeMillis())}"
        binding.editCatatan.setText(dataUpdate?.catatan ?: "")


        hourStart = dataUpdate?.start_time
        hourEnd = dataUpdate?.end_time
        duration = dataUpdate?.duration
        operator = dataUpdate?.operator
        firstTotalisator = dataUpdate?.totalisator_awal
        lastTotalisator = dataUpdate?.totalisator_akhir
        mVariabel = dataUpdate?.m_number
        fromTank = dataUpdate?.tanki
        noBridger = dataUpdate?.snr_no
        totalLiter = dataUpdate?.jumlah_topping
        dippingAfterTopping = dataUpdate?.hasil_dipstik
        note = dataUpdate?.catatan

    }

    private fun insertData(){
        operator = binding.spinnerOperatorName.text.toString()
        noBridger = binding.editNomorBridger.text.toString()
        totalLiter = binding.editToppingVolume.text.toString().replace(".","").toInt()
        firstTotalisator = binding.editTotalisatorAwal.text.toString()
        lastTotalisator = binding.editTotalisatorAkhir.text.toString()
        mVariabel = binding.spinnerM.text.toString()
        fromTank = binding.spinnerTankQc.text.toString()
        note = binding.editCatatan.text.toString()
        dippingAfterTopping = binding.editDippingAfterTopping.text.toString().toDouble()





        val data = ToppingUp(
            start_time = hourStart,
            end_time = hourEnd,
            duration = duration,
            operator = operator,
            snr_no = noBridger,
            jumlah_topping = totalLiter,
            totalisator_awal = firstTotalisator,
            totalisator_akhir = lastTotalisator,
            m_number = mVariabel,
            tanki = fromTank,
            catatan = note,
            hasil_dipstik = dippingAfterTopping,
            sales_ref = 0,
            sisa_dipping = 0,
            status = STATUS
        )

        if (dataUpdate == null){
            mainViewModel.insert(toppingUp = data)
            Toast.makeText(requireContext(),"Berhasil Melakukan Input Data", Toast.LENGTH_LONG).show()

        }else{
            val dataForUpdate = ToppingUp(
                id = dataUpdate?.id!!,
                start_time = hourStart,
                end_time = hourEnd,
                duration = duration,
                operator = operator,
                snr_no = noBridger,
                jumlah_topping = totalLiter,
                totalisator_awal = firstTotalisator,
                totalisator_akhir = lastTotalisator,
                m_number = mVariabel,
                tanki = fromTank,
                catatan = note,
                hasil_dipstik = dippingAfterTopping,
                sales_ref = 0,
                sisa_dipping = 0,
                status = STATUS
            )
            Log.d("UPDATE DATA","$dataForUpdate")
            Toast.makeText(requireContext(),"Berhasil Melakukan Update Data", Toast.LENGTH_LONG).show()
            mainViewModel.updateToppingUp(dataForUpdate)
            activity?.finish()
        }


    }


    private fun pickupTime(context: Context){
        val dialogView = layoutInflater.inflate(R.layout.layout_picker_duration,null)
        val hourPicker  = dialogView.findViewById<NumberPicker>(com.syamsudinnoor.aft.aviation.pertamina.R.id.hourPicker)
        val minutePicker = dialogView.findViewById<NumberPicker>(com.syamsudinnoor.aft.aviation.pertamina.R.id.minutePicker)

        hourPicker.minValue = 0
        hourPicker.maxValue = 2

        minutePicker.minValue = 0
        minutePicker.maxValue = 60

        val titleView = TextView(requireContext()).apply {
            text = "Total Waktu Pengisian"
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setTypeface(typeface, Typeface.BOLD)
        }

        AlertDialog.Builder(context)
            .setCustomTitle(titleView)
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

    override fun onResume() {
        super.onResume()

        val operators = resources.getStringArray(R.array.operator_choice)
        val mChoice = resources.getStringArray(R.array.m_choice)
        val tanks = resources.getStringArray(R.array.tangks_choice)

        val adapterOperators = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, operators)
        val adapterM = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, mChoice)
        val adapterTanks = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, tanks)

        binding.spinnerTankQc.setAdapter(adapterTanks)
        binding.spinnerOperatorName.setAdapter(adapterOperators)
        binding.spinnerM.setAdapter(adapterM)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    companion object{
        const val STATUS = "konsinyasi"

        fun newInstance(toppingUp : ToppingUp?) = KonsinyasiFragment().apply {
            arguments = Bundle().apply {
                putParcelable("UPDATE_DATA",toppingUp)
            }
        }
    }

}