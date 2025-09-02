package com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.fragment

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.CardViewHolderBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentVolumeControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.KompartemenHolderBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlMainViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.dateDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.textWatcherWithNumber
import java.util.Date

class VolumeControlFragment : Fragment() {

    private  var _binding : FragmentVolumeControlBinding? = null

    private val viewModel: VolumeControlViewModel by viewModels{
        val database = SnoorRoomDatabase.getDatabase(requireContext())
        val dao = SNoorRepository(database.sNoorDao())
        ViewModelFactory(dao)
    }


    private val volumeViewModel : VolumeControlMainViewModel by viewModels{
        val database = MainDatabase.getDatabase(requireContext())
        val dao = MainRepository(database.getDao())
        MainViewModelFactory(dao)
    }
    private val binding get() = _binding!!

    private val _kompartemenStatus :  Array<String> = arrayOf("Depan", "Tengah","Belakang")
    private val kompartemenStatus = mutableSetOf<String>()
    private lateinit var adapter1 : ArrayAdapter<String>
    private lateinit var adapter2 : ArrayAdapter<String>
    private lateinit var adapter3 : ArrayAdapter<String>



    //header data
    private var tanggalBongkar : Long? = null
    private var aft : String? = null
    private var supplyPoint : String? = null
    private var transportir : String? = null
    private var noPolisi : String? = null
    private var kuantitas : Int? = null
    private var hargaAvtur : Double? = null
    private var spvRsd : String? = null
    private var sopirBridger1 : String? = null
    private var sopirBridger2 : String? = null

    private val arrayKompartemen : MutableList<String?> = mutableListOf("","","")
    private val arrayTera : MutableList<Int?> = mutableListOf(0, 0, 0)
    private val arrayUkuranSupplyPoint : MutableList<Int?> = mutableListOf(0, 0, 0)
    private val arrayRmmI : MutableList<Double?> = mutableListOf(0.0,0.0,0.0)
    private val arrayUkuranDppu : MutableList<Int?> = mutableListOf(0, 0, 0)
    private val arrayDensityObs : MutableList<Double?> = mutableListOf(0.0,0.0,0.0)
    private val arrayTempObs : MutableList<Int?> = mutableListOf(0,0,0)

    private val arraySelisihUllage : MutableList<Int?> = mutableListOf(0,0,0)
    private val arraySelisihLiter : MutableList<Double?> = mutableListOf(0.0,0.0,0.0)

    private val arrayLiter15 : MutableList<Double?> = mutableListOf(0.0,0.0,0.0)
    private val arrayDensity15 : MutableList<Double> = mutableListOf(0.0,0.0,0.0)
    private val arrayCorrFactor  = mutableListOf(0.0,0.0,0.0)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adapter1 = ArrayAdapter(requireContext(), R.layout.spinner_item_holder,_kompartemenStatus.toMutableList())
        adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item_holder,_kompartemenStatus.toMutableList())
        adapter3 = ArrayAdapter(requireContext(), R.layout.spinner_item_holder,_kompartemenStatus.toMutableList())

        _binding = FragmentVolumeControlBinding.inflate(inflater, container, false)
        observeCorrFactor()


        binding.kompartemen1.buttonSearchKompartemen.setOnClickListener {
            checkValidation("Depan",binding.kompartemen1)
        }

        binding.kompartemen1.buttonNextKompartemen.setOnClickListener {
            binding.txtKompartemen2.visibility = View.VISIBLE
            binding.kompartemen2.lvKompartemen1.visibility = View.VISIBLE
            binding.searchHolder2.cardViewResult.visibility = View.VISIBLE
            binding.buttonSaveData.isEnabled = false
        }

        binding.kompartemen2.buttonSearchKompartemen.setOnClickListener {
            checkValidation("Tengah",binding.kompartemen2)
        }

        binding.kompartemen2.buttonNextKompartemen.setOnClickListener {
            binding.txtKompartemen3.visibility = View.VISIBLE
            binding.kompartemen3.lvKompartemen1.visibility = View.VISIBLE
            binding.searchHolder3.cardViewResult.visibility = View.VISIBLE
            binding.buttonSaveData.isEnabled = false
        }


        binding.kompartemen3.buttonSearchKompartemen.setOnClickListener {
            checkValidation("Belakang",binding.kompartemen3)
        }

        binding.buttonTangalBongkar.setOnClickListener {
            dateDialog(requireContext()) { year, month, day ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                tanggalBongkar = calendar.timeInMillis

                binding.buttonTangalBongkar.setText(TimeConverter.toReadableDate(tanggalBongkar!!))
            }
        }
        binding.buttonSaveData.setOnClickListener {
            checkMainInput()
        }

        binding.editHargaAvtur.addTextChangedListener(textWatcherWithNumber(binding.editHargaAvtur))

        return binding.root
    }

    private fun checkMainInput() {
        aft = binding.spinnerAft.text.toString()
        supplyPoint = binding.spinnerSupplyPoint.text.toString()
        transportir = binding.spinnerTransportir.text.toString()
        noPolisi = binding.editNoPolisi.text.toString()
        kuantitas = binding.editKuantitas.text.toString().toIntOrNull()
        hargaAvtur = binding.editHargaAvtur.text.toString().replace(".","").toDoubleOrNull()
        spvRsd = binding.editSpvRsd.text.toString()
        sopirBridger1 = binding.editSopirBridger1.text.toString()
        sopirBridger2 = binding.editSopirBridger2.text.toString()



        val volumeControl = AnalisaVolumeControlQuality(
            tanggal = tanggalBongkar,
            aft = aft,
            supply_point = supplyPoint,
            transportir = transportir,
            kuantitas = kuantitas,
            no_polisi = noPolisi,
            harga_avtur = hargaAvtur,
            spv_rsd = spvRsd,
            sopir_bridger_1 = sopirBridger1,
            sopir_bridger_2 = sopirBridger2
        )
        val detail = mutableListOf<DetailKompartemen>()
        for (i in getNonNullDataIndex(arrayKompartemen)){
            val data = DetailKompartemen(
                kompartemen = arrayKompartemen[i],
                tera = arrayTera[i],
                ukuran_supply_point = arrayUkuranSupplyPoint[i],
                rmm_i = arrayRmmI[i],
                ukuran_dppu = arrayUkuranDppu[i],
                density_obs = arrayDensityObs[i],
                temp_obs = arrayTempObs[i],
                selisih_ullage = arraySelisihUllage[i],
                selisih_liter = arraySelisihLiter[i],
                density_15 = arrayDensity15[i],
                liter_15 = arrayLiter15[i],
                corr_factor = arrayCorrFactor[i],
                idAnalisa = 0
            )
            detail.add(data)
        }

        Toast.makeText(requireContext(), "Data berhasil disimpan ${detail}", Toast.LENGTH_SHORT).show()

        volumeViewModel.insertVolumeWithDetail(volumeControl, detail)

    }

    private fun getNonNullDataIndex(list: MutableList<String?>): List<Int> {
        return list.mapIndexedNotNull { index, value ->
            if (value!!.isNotEmpty() || value.isNotBlank()){
                index
            }else{
                null
            }
        }
    }

    private fun checkValidation(status: String, kompartemenHolder: KompartemenHolderBinding) {
        val numerator = when{status == "Depan" -> 0; status == "Tengah" -> 1 else -> 2}

        arrayKompartemen[numerator] = kompartemenHolder.spinnerKompartemen.text.toString()
        arrayTera[numerator] = kompartemenHolder.editTeraKompartemen.text.toString().toIntOrNull()
        arrayUkuranSupplyPoint[numerator]= kompartemenHolder.editUkuranSuppyPointKompartemen.text.toString().toIntOrNull()
        arrayRmmI[numerator]  = kompartemenHolder.editRMmIKompartemen.text.toString().toDoubleOrNull()
        arrayUkuranDppu[numerator] = kompartemenHolder.editUkuranDppuKompartemen.text.toString().toIntOrNull()
        arrayDensityObs[numerator] = kompartemenHolder.editDensityObsKompartemen.text.toString().toDoubleOrNull()
        arrayTempObs[numerator] = kompartemenHolder.editTempObsKompartemen.text.toString().toIntOrNull()
        
        if (arrayKompartemen[numerator] != null && 
            (arrayTera[numerator] != null || arrayUkuranSupplyPoint[numerator] != null) &&
            arrayRmmI[numerator] != null && arrayUkuranDppu[numerator] != null &&
            arrayDensityObs[numerator] != null && arrayTempObs[numerator] != null){
            arraySelisihUllage[numerator] = selisihUllage(arrayTera[numerator] ?: 0,
                arrayUkuranSupplyPoint[numerator] ?: 0,
                arrayUkuranDppu[numerator] ?: 0)

            arraySelisihLiter[numerator] = selisihLiter(arraySelisihUllage[numerator] ?: 0, arrayRmmI[numerator] ?: 0.0)
            submitDataViewModel(status, arrayDensityObs[numerator] ?: 0.0, arrayTempObs[numerator] ?: 0)
            kompartemenHolder.buttonNextKompartemen.isEnabled = true
            binding.buttonSaveData.isEnabled = true

            Toast.makeText(requireContext(), "Selisih Ullage : ${arraySelisihUllage[numerator]} liter selisih : ${arraySelisihLiter[numerator]} corr : ${arrayCorrFactor[numerator]} selisih liter : ${arrayLiter15[numerator]} -- ${selisihLiter15(arrayCorrFactor[numerator],arraySelisihLiter[numerator] ?: 0.0)}", Toast.LENGTH_LONG).show()


        }else{
            Toast.makeText(requireContext(), "Mohon lengkapi data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSearch(holder :  CardViewHolderBinding,status: String){
        holder.imgResultIcon.visibility = View.GONE
        holder.tableViewResult.visibility = View.VISIBLE
        holder.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)

        val numerator = when{status == "Depan" -> 0; status == "Tengah" -> 1 else -> 2}

        holder.row1Header.text = resources.getString(R.string.ullage_tank)
        holder.row1Body.text = arraySelisihUllage[numerator].toString()

        holder.row2Header.text = "Selisih Liter"
        holder.row2Body.text = arraySelisihLiter[numerator].toString()

        holder.row3Header.text = "Density 15"
        holder.row3Body.text = arrayDensity15[numerator].toString()

        val rMmI : Double = arraySelisihLiter[numerator] ?: 0.0
        val result = selisihLiter15(arrayCorrFactor[numerator],rMmI)
        holder.row4Header.text = "Liter 15"
        holder.row4Body.text = result.toString()

        arrayLiter15[numerator] = result

    }


    private fun selisihUllage(tera : Int, ukuranSupplyPoint : Int, ukuranDDPU : Int) : Int{
        return if (ukuranSupplyPoint != 0) ukuranDDPU - ukuranSupplyPoint else ukuranDDPU - tera
    }

    private fun selisihLiter(selisihUllage: Int, mm : Double) : Double{
        return if (selisihUllage != 0) selisihUllage / mm else 0.0
    }


    private fun observeCorrFactor(){
        viewModel.corrFactorDepan.observe(viewLifecycleOwner){ corrFactor ->
            if (corrFactor != null){
                arrayCorrFactor[0] = corrFactor.correctionFactor ?: 0.0
                arrayDensity15[0] = corrFactor.density15 ?: 0.0
                showSearch(binding.searchHolder1, "Depan")
            }else{
                binding.searchHolder1.imgResultIcon.visibility = View.VISIBLE
                binding.searchHolder1.tableViewResult.visibility = View.GONE
                binding.searchHolder1.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

        viewModel.corrFactorTengah.observe(viewLifecycleOwner){ corrFactor ->
            if (corrFactor != null){
                arrayCorrFactor[1] = corrFactor.correctionFactor ?: 0.0
                arrayDensity15[1] = corrFactor.density15 ?: 0.0
                showSearch(binding.searchHolder2, "Tengah")
            }else{
                binding.searchHolder2.imgResultIcon.visibility = View.VISIBLE
                binding.searchHolder2.tableViewResult.visibility = View.GONE
                binding.searchHolder2.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

        viewModel.corrFactorBelakang.observe(viewLifecycleOwner){ corrFactor ->
            if (corrFactor != null){
                arrayCorrFactor[2] = corrFactor.correctionFactor ?: 0.0
                arrayDensity15[2] = corrFactor.density15 ?: 0.0
                showSearch(binding.searchHolder3, "Belakang")
            }else{
                binding.searchHolder3.imgResultIcon.visibility = View.VISIBLE
                binding.searchHolder3.tableViewResult.visibility = View.GONE
                binding.searchHolder3.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }
    }

    private fun submitDataViewModel(status : String, density : Double, temp : Int){
        val stringfyInput = (density.toString() + temp.toString()).replace(".",",")
        when(status){
            "Depan" -> viewModel.getCorrFactorDepan(stringfyInput)
            "Tengah" -> viewModel.getCorrFactorTengah(stringfyInput)
            "Belakang" -> viewModel.getCorrFactorBelakang(stringfyInput)
        }
    }

    private fun selisihLiter15(corrFactor: Double, selisihLiter: Double) : Double = corrFactor * selisihLiter

    private fun updateDropDown(){
        val filteredList = _kompartemenStatus.filter { it !in kompartemenStatus }

        if(binding.kompartemen1.spinnerKompartemen.text.toString().isEmpty()){
            adapter1.clear()
            adapter1.addAll(filteredList)
        }else if(binding.kompartemen2.spinnerKompartemen.text.toString().isEmpty()){
            adapter2.clear()
            adapter2.addAll(filteredList)
        }else if(binding.kompartemen3.spinnerKompartemen.text.toString().isEmpty()) {
            adapter3.clear()
            adapter3.addAll(filteredList)
        }
    }

    private fun setupListener(dropdown : AutoCompleteTextView, adapter : ArrayAdapter<String>){
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            if (selectedItem != null) {
                kompartemenStatus.add(selectedItem)
                updateDropDown()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.kompartemen1.spinnerKompartemen.setAdapter(adapter1)
        binding.kompartemen2.spinnerKompartemen.setAdapter(adapter2)
        binding.kompartemen2.spinnerKompartemen.setAdapter(adapter3)

        setupListener(binding.kompartemen1.spinnerKompartemen, adapter1)
        setupListener(binding.kompartemen2.spinnerKompartemen, adapter2)
        setupListener(binding.kompartemen3.spinnerKompartemen, adapter3)

        val arrayAft = arrayOf("AFT. Syamsudin Noor")
        val arraySupplyPoint = arrayOf("IT Banjarmasin")
        val arrayTransportir = arrayOf("PT. Pertamina Patra Niaga")

        val adapterAft = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, arrayAft)
        val adapterSupplyPoint = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, arraySupplyPoint)
        val adapterTransportir = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, arrayTransportir)

        binding.spinnerAft.setAdapter(adapterAft)
        binding.spinnerSupplyPoint.setAdapter(adapterSupplyPoint)
        binding.spinnerTransportir.setAdapter(adapterTransportir)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


