package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter

class QualityControlFragment : Fragment() {

    private var _binding : FragmentQualityControlBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DensityViewModel by viewModels {
        val database = SnoorRoomDatabase.Companion.getDatabase(requireContext())
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }

    private val mainViewModel : QualityControlViewModel by viewModels{
        val database = MainDatabase.Companion.getDatabase(requireContext())
        val repository = MainRepository(database.getDao())
        MainViewModelFactory(repository)
    }

    private var currentTime = System.currentTimeMillis()
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

    //non nullable
    private var densityOBDS : Double? = 0.0
    private var temperature : Double? = 0.0

    private var resultDensity : Double? = 0.0
    private var diffMax : Double? = 0.0
    private var appmNo : String? = null
    private var cuPSM : String? = null
    private var toTank : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentQualityControlBinding.inflate(inflater, container, false)

        binding.editTime.setText(TimeConverter.toReadableTime(currentTime))

        //setupDensity()
        binding.buttonSearch.isEnabled = true

        viewModel.joinChecking.observe(viewLifecycleOwner){ isValid ->
            binding.buttonSave.isEnabled = isValid
        }

        binding.buttonSearch.setOnClickListener {
            checkInput()
        }

        binding.buttonSave.setOnClickListener {
            val reportData = BridgerQualityControl(
                dateTime = currentTime,
                bridger_no = bridgerNo,
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
                operator_name = operatorName
            )
            mainViewModel.insert(reportData)
            viewModel.onJoinChecking(false)
            currentTime = System.currentTimeMillis()
            binding.editTime.setText(TimeConverter.toReadableTime(currentTime))
            Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun checkInput(){
        operatorName = binding.spinnerOperatorName.text.toString()
        bridgerNo = binding.editBridgerNo.text.toString()
        BppNo = binding.editBpp.text.toString()
        liter = binding.editLiter.text.toString().toDoubleOrNull()
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


        if (densityFromDistributor != null && densityOBDS != null && temperature != null) {

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
        if (density != null && temperature != null){
            viewModel.searchDensity(density, temperature)
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
            val formattedDifference = String.format("%.5f", difference)


            val status: String
            if (difference in NEGATIVE_TOLERENCE..TOLERENCE) {
                status = "OKE"
            } else {
                status = "NOT OKE"
            }

            binding.apply {
                textViewResult.text = "Hasil: $resultValue, Diferensial: $formattedDifference | Status: $status"
                imgResultIcon.visibility = View.GONE
            }

            resultDensity = resultValue
            diffMax = formattedDifference.replace(",",".").toDouble()

        }
    }




    override fun onResume() {
        super.onResume()
        val resouceOperator = resources.getStringArray(R.array.operator_choice)
        val resourceTank = resources.getStringArray(R.array.tangks_choice)

        val adapterOperator =
            ArrayAdapter(requireContext(), R.layout.spinner_item_holder, resouceOperator)
        val adapterTank =
            ArrayAdapter(requireContext(), R.layout.spinner_item_holder, resourceTank)

        binding.spinnerOperatorName.setAdapter(adapterOperator)
        binding.spinnerTankQc.setAdapter(adapterTank)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val TOLERENCE : Double = 0.003
        const val NEGATIVE_TOLERENCE : Double = -0.003
    }


}