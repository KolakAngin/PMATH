package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.DensityViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.QualityControlViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumberDecimal
import com.syamsudinnoor.aft.aviation.pertamina.utility.helperSettingEditText
import com.syamsudinnoor.aft.aviation.pertamina.utility.textWatcherWithNumber
import java.util.Date
import kotlin.toString

class QualityControlFragment : Fragment() {

    private var _binding : FragmentQualityControlBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DensityViewModel by viewModels {
        val database = SnoorRoomDatabase.getDatabase(requireContext())
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }

    private val mainViewModel : QualityControlViewModel by viewModels{
        val database = MainDatabase.getDatabase(requireContext())
        val repository = MainRepository(database.getDao())
        MainViewModelFactory(repository)
    }

    private var currentTime : Long? = null
    private var density15 : Double? = null
    private var resultByDensity15 : Double? = null


    private var operatorName : String? = null
    private var bridgerNo : String? = null
    private var BppNo : String? = null
    private var liter : Double? = null
    private var sealOrSegel : String? = null
    private var testReportNo : String? = null

    //non nullable
    private var densityFromDistributor : Double? = 0.0

    private var afrnNo : String? = null
    private var densityOBDSDocument : Double? = 0.0
    private var temperatureDocument : Double? = 0.0
    private var resultDensityDocument : Double? = 0.0

    private var densityOBDS : Double? = 0.0
    private var temperature : Double? = 0.0

    private var resultDensity : Double? = 0.0
    private var diffMax : Double? = 0.0
    private var appmNo : String? = null
    private var cuPSM : String? = null
    private var toTank : String? = null
    private var note : String? = null

    private var dataUpdate : BridgerQualityControl? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentQualityControlBinding.inflate(inflater, container, false)

        binding.buttonTimeFrom.setOnClickListener {
            timeDialog(requireContext(),"Start Time") { hour, minute ->
                val date = Date()
                currentTime = TimeConverter.concatenateStringDate(date, hour.toString(),minute.toString())
                binding.buttonTimeFrom.setText(TimeConverter.toReadableTime(currentTime!!))
            }
        }

        dataUpdate = if (Build.VERSION.SDK_INT >= 33){
            arguments?.getParcelable("UPDATE_DATA",BridgerQualityControl::class.java)
        }else{
            @Suppress("DEPRECATION") arguments?.getParcelable("UPDATE_DATA")
        }

        if (dataUpdate != null){
            setupAllData(dataUpdate!!)
        }

        //setupDensity()
        binding.buttonSearch.isEnabled = true

        viewModel.joinChecking.observe(viewLifecycleOwner){ isValid ->
            binding.buttonSave.isEnabled = isValid
        }

        binding.buttonSearch.setOnClickListener {
            checkInput()
        }
        binding.editCuPsm.addTextChangedListener(helperSettingEditText {
            if (it.isNotEmpty()){
                when{
                    it.toDouble() in 50.0..800.0 -> {
                        Log.d("cuPSM",it)
                    }else ->{
                    Log.d("cuPSM",it + " Gagal")
                    binding.editCuPsm.error = "Harus di antara 50 - 800"
                }
                }
            }
        })
        binding.spinnerLiter.apply { addTextChangedListener(textWatcherWithNumber(this@apply)) }

        binding.buttonSave.setOnClickListener {
            val wasUpper = when{
                bridgerNo == null -> ""
                else -> bridgerNo?.uppercase()
            }
            val reportData = BridgerQualityControl(
                dateTime = currentTime,
                bridger_no = wasUpper,
                bpp = BppNo,
                volume_liter = liter,
                seal = sealOrSegel,
                test_report_no = testReportNo,
                density_15_from_distributor = densityFromDistributor,
                afrn_no = afrnNo,
                density_obsd_rec_document = densityOBDSDocument,
                temp_rec_document = temperatureDocument,
                density_15_result_rec_document = resultDensityDocument,
                density_obsd = densityOBDS,
                temprature = temperature,
                density_15_calculation = resultDensity,
                diff_from_density_distributor = diffMax,
                app_star = appmNo,
                cu_psm = cuPSM?.toDoubleOrNull(),
                tangki = toTank,
                operator_name = operatorName,
                catatan = note
            )


            if (dataUpdate == null){
                mainViewModel.insert(reportData)
                Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            }else{
                val reportDataToUpdate = BridgerQualityControl(
                    id = dataUpdate?.id!!,
                    dateTime = currentTime,
                    bridger_no = wasUpper,
                    bpp = BppNo,
                    volume_liter = liter,
                    seal = sealOrSegel,
                    test_report_no = testReportNo,
                    density_15_from_distributor = densityFromDistributor,
                    afrn_no = afrnNo,
                    density_obsd_rec_document = densityOBDSDocument,
                    temp_rec_document = temperatureDocument,
                    density_15_result_rec_document = resultDensityDocument,
                    density_obsd = densityOBDS,
                    temprature = temperature,
                    density_15_calculation = resultDensity,
                    diff_from_density_distributor = diffMax,
                    app_star = appmNo,
                    cu_psm = cuPSM?.toDoubleOrNull(),
                    tangki = toTank,
                    operator_name = operatorName,
                    catatan = note
                )
                mainViewModel.update(reportDataToUpdate)
                Toast.makeText(requireContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
            viewModel.onJoinChecking(false)
            currentTime = System.currentTimeMillis()

        }

        binding.spinnerBridgerNo.addTextChangedListener(helperSettingEditText {
            setupQuantity(it)
        })

        return binding.root
    }

    private fun setupQuantity(noBridger : String) {
        when(noBridger){
            "B 9854 SFV" ->{
                binding.spinnerLiter.setText("16.000")
            }
            "KB 8137 AV" ->{
                binding.spinnerLiter.setText("24.000")
            }
            "B 9280 SFU" ->{
                binding.spinnerLiter.setText("16.000")
            }
            "DA 8295 PZ" ->{
                binding.spinnerLiter.setText("16.000")
            }
            "DA 8294 PZ" ->{
                binding.spinnerLiter.setText("16.000")
            }
        }

    }

    private fun setupAllData(data : BridgerQualityControl){

        binding.spinnerOperatorName.setText(data.operator_name ?: "")
        binding.spinnerBridgerNo.setText(data.bridger_no ?: "")
        binding.buttonTimeFrom.setText(TimeConverter.toReadableTime(data.dateTime ?: System.currentTimeMillis()))
        binding.editBpp.setText(data.bpp ?: "")
        binding.spinnerLiter.setText(data.volume_liter?.toInt()?.toString() ?: "")
        binding.editSeal.setText(data.seal ?: "")
        binding.editTestReport.setText(data.test_report_no ?: "")
        binding.editDens15FromDistributor.setText(data.density_15_from_distributor?.toString() ?: "")
        binding.afrnNo.setText(data.afrn_no ?: "")
        binding.editDensityRecDoc.setText(data.density_obsd_rec_document?.toString() ?: "")
        binding.editTempRecDoc.setText(data.temp_rec_document?.toString() ?: "")
        binding.editResultDensityRecDoc.setText(data.density_15_result_rec_document?.toString() ?: "")
        binding.editTextDensity.setText(data.density_obsd?.toString() ?: "")
        binding.editTextTemp.setText(data.temprature?.toString() ?: "")
        binding.editApp.setText(data.app_star ?: "")
        binding.editCuPsm.setText(data.cu_psm?.toInt()?.toString() ?: "")
        binding.spinnerTankQc.setText(data.tangki ?: "")
        binding.txtCatatan.setText(data.catatan ?: "")

        currentTime = data.dateTime
        bridgerNo = data.bridger_no
        BppNo = data.bpp
        liter = data.volume_liter
        sealOrSegel = data.seal
        testReportNo = data.test_report_no
        densityFromDistributor = data.density_15_from_distributor
        afrnNo = data.afrn_no
        densityOBDSDocument = data.density_obsd_rec_document
        temperatureDocument = data.temp_rec_document
        resultDensityDocument = data.density_15_result_rec_document
        densityOBDS = data.density_obsd
        temperature = data.temprature
        appmNo = data.app_star
        cuPSM = data.cu_psm.toString()
        toTank = data.tangki
        operatorName = data.operator_name
        note = data.catatan
    }

    private fun checkInput(){
        binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
        operatorName = binding.spinnerOperatorName.text.toString()
        bridgerNo = binding.spinnerBridgerNo.text.toString()
        BppNo = binding.editBpp.text.toString()
        liter = binding.spinnerLiter.text.toString().replace(".","").toDoubleOrNull()
        sealOrSegel = binding.editSeal.text.toString()
        testReportNo = binding.editTestReport.text.toString()

        //non nullable
        densityFromDistributor = binding.editDens15FromDistributor.text.toString().toDoubleOrNull()

        afrnNo = binding.afrnNo.text.toString()
        densityOBDSDocument = binding.editDensityRecDoc.text.toString().toDoubleOrNull()
        temperatureDocument = binding.editTempRecDoc.text.toString().toDoubleOrNull()
        resultDensityDocument = binding.editResultDensityRecDoc.text.toString().toDoubleOrNull()

        //non nullable
        densityOBDS = binding.editTextDensity.text.toString().toDoubleOrNull()
        temperature = binding.editTextTemp.text.toString().toDoubleOrNull()

        appmNo = binding.editApp.text.toString()
        cuPSM = binding.editCuPsm.text.toString()
        toTank = binding.spinnerTankQc.text.toString()
        note = binding.txtCatatan.text.toString()

        val cuPSM = binding.editCuPsm.text.toString().toDoubleOrNull()
        val cu = when(cuPSM != null){
            true -> cuPSM
            false -> 0.0
        }

        if (densityOBDSDocument != null && temperatureDocument != null){
            searchDensityDoc()
        }

        if (densityFromDistributor != null && densityOBDS != null && temperature != null && cu in 50.0..800.0) {

            searchDensity()
            setupDensity()
            viewModel.onJoinChecking(true)

        }else{
            Toast.makeText(requireContext(), "Data tidak lengkap", Toast.LENGTH_SHORT).show()
            viewModel.onJoinChecking(false)
        }
    }

    private fun searchDensity() {
        val density = binding.editTextDensity.text.toString().toDoubleOrNull()
        val temperature = binding.editTextTemp.text.toString().toDoubleOrNull()

        if (density != null && temperature != null) {
            viewModel.searchDensity(density, temperature)
        }
    }

    private fun searchDensityDoc() {
        val densityDoc = binding.editDensityRecDoc.text.toString().toDoubleOrNull()
        val temperature = binding.editTempRecDoc.text.toString().toDoubleOrNull()
        if (densityDoc != null && temperature != null) {
            viewModel.searchDensityDoc(densityDoc, temperature)
        }

        viewModel.densityResultDoc.observe(viewLifecycleOwner){
            binding.editResultDensityRecDoc.setText(it?.result.toString())
        }
    }

    private fun setupDensity() {

        viewModel.densityResult.observe(viewLifecycleOwner) { result ->
            if (result == null || result.result == null) {
                binding.textViewResult.text = "Data tidak ditemukan."
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
                binding.imgResultIcon.visibility = View.VISIBLE

                return@observe
            }


            val distributorDensity = densityFromDistributor

            if (distributorDensity == null) {
                binding.textViewResult.text = "Hasil Ditemukan: ${result.result}"
                binding.imgResultIcon.visibility = View.GONE
                return@observe
            }

            val resultValue = result.result
            val difference = resultValue - distributorDensity

            // 4. Bulatkan hasil pengurangan menjadi 5 digit di belakang koma untuk ditampilkan
            val formattedDifference = formatterNumberDecimal(difference,6)


            val status: String
            if (difference in NEGATIVE_TOLERENCE..TOLERENCE) {
                status = "OKE"
            } else {
                status = "NOT OKE"
            }

            when (status) {
                "OKE" -> {
                    binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
                }
                "NOT OKE" -> {
                    binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
                }
            }

            binding.apply {
                textViewResult.text = "Hasil: $resultValue, Diferensial: $formattedDifference | Status: $status"
                imgResultIcon.visibility = View.GONE
            }

            resultDensity = resultValue
            diffMax = formattedDifference?.replace(",",".")?.toDouble()

        }
    }


    override fun onResume() {
        super.onResume()
        val resouceOperator = resources.getStringArray(R.array.operator_choice)
        val resourceTank = resources.getStringArray(R.array.tangks_choice)
        val numBridger = resources.getStringArray(R.array.bridger_choice)
        val arrayQuantity = resources.getStringArray(R.array.bridger_quantity_choice)

        val adapterOperator =
            ArrayAdapter(requireContext(), R.layout.spinner_item_holder, resouceOperator)
        val adapterTank =
            ArrayAdapter(requireContext(), R.layout.spinner_item_holder, resourceTank)
        val adapterBridger =
            ArrayAdapter(requireContext(),R.layout.spinner_item_holder,numBridger)
        val adapterQuantity =
            ArrayAdapter(requireContext(),R.layout.spinner_item_holder,arrayQuantity)

        binding.spinnerOperatorName.setAdapter(adapterOperator)
        binding.spinnerTankQc.setAdapter(adapterTank)
        binding.spinnerBridgerNo.setAdapter(adapterBridger)
        binding.spinnerLiter.setAdapter(adapterQuantity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{

        fun newInstance(bridgerQualityControl: BridgerQualityControl?) = QualityControlFragment().apply {
            arguments = Bundle().apply {
                putParcelable("UPDATE_DATA",bridgerQualityControl)
            }
        }
        const val TOLERENCE : Double = 0.003
        const val NEGATIVE_TOLERENCE : Double = -0.003
    }


}