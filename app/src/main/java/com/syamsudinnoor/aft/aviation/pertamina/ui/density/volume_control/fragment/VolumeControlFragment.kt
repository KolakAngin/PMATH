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
import androidx.core.widget.addTextChangedListener
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
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlMainViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.dateDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.helperSettingEditText
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter
import com.syamsudinnoor.aft.aviation.pertamina.utility.settingDialogGlobal
import com.syamsudinnoor.aft.aviation.pertamina.utility.textWatcherWithNumber
import java.sql.Time
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

    private var note : String? = null

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

    private lateinit var kompartemenView : Array<KompartemenHolderBinding>
    private lateinit var searcHolderView : Array<CardViewHolderBinding>
    private var updateData : AnalisaVolumeControlWithDetail? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVolumeControlBinding.inflate(inflater, container, false)
        observeCorrFactor()

        kompartemenView = arrayOf(binding.kompartemen1, binding.kompartemen2, binding.kompartemen3)
        searcHolderView = arrayOf(binding.searchHolder1, binding.searchHolder2, binding.searchHolder3)
        kompartemenView.last().buttonNextKompartemen.visibility = View.GONE


        updateData = arguments?.getParcelable<AnalisaVolumeControlWithDetail?>("UPDATE_DATA")

        if (updateData != null){
            setupUpdateData(updateData!!)
        }


        binding.kompartemen1.buttonSearchKompartemen.setOnClickListener {
            checkValidation("Depan",binding.kompartemen1)
        }
        binding.editHargaAvtur.apply { addTextChangedListener ( textWatcherWithNumber(this@apply) ) }
        binding.spinnerKuantitas.apply { addTextChangedListener ( textWatcherWithNumber(this@apply) ) }

        binding.spinnerNoPolisi.apply { addTextChangedListener(helperSettingEditText {
            if (it.isNotEmpty()){
                setMMFilled(it)
            }
        }) }

        binding.kompartemen1.buttonNextKompartemen.setOnClickListener {
            settingDialogGlobal("Konfirmasi Tambah Data Kompartemen",
                "Apakah Anda yakin tambah data kompartemen?",
                requireContext()){
                binding.txtKompartemen2.visibility = View.VISIBLE
                binding.kompartemen2.lvKompartemen1.visibility = View.VISIBLE
                binding.searchHolder2.cardViewResult.visibility = View.VISIBLE
                binding.buttonSaveData.isEnabled = false
            }
        }

        binding.kompartemen2.buttonSearchKompartemen.setOnClickListener {
            checkValidation("Tengah",binding.kompartemen2)
        }

        binding.kompartemen2.buttonNextKompartemen.setOnClickListener {
            settingDialogGlobal("Konfirmasi Tambah Data Kompartemen",
                "Apakah Anda yakin tambah data kompartemen?",
                requireContext()){
                binding.txtKompartemen3.visibility = View.VISIBLE
                binding.kompartemen3.lvKompartemen1.visibility = View.VISIBLE
                binding.searchHolder3.cardViewResult.visibility = View.VISIBLE
                binding.kompartemen3.buttonNextKompartemen.visibility = View.GONE
            }

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

        return binding.root
    }

    private fun setMMFilled(noBridger : String){

        when(noBridger){
            "B 9854 SFV" ->{
                binding.kompartemen1.spinnerRMmIKompartemen.setText(0.205.toString())
                binding.kompartemen2.spinnerRMmIKompartemen.setText(0.35.toString())
                binding.spinnerKuantitas.setText("16.000")
            }
            "KB 8137 AV" ->{
                binding.kompartemen1.spinnerRMmIKompartemen.setText(0.25.toString())
                binding.kompartemen2.spinnerRMmIKompartemen.setText(0.25.toString())
                binding.kompartemen3.spinnerRMmIKompartemen.setText(0.25.toString())
                binding.spinnerKuantitas.setText("24.000")
            }
            "B 9280 SFU" ->{
                binding.kompartemen1.spinnerRMmIKompartemen.setText(0.3.toString())
                binding.kompartemen2.spinnerRMmIKompartemen.setText(0.3.toString())
                binding.spinnerKuantitas.setText("16.000")
            }
            "DA 8295 PZ" ->{
                binding.kompartemen1.spinnerRMmIKompartemen.setText(0.305.toString())
                binding.kompartemen2.spinnerRMmIKompartemen.setText(0.34.toString())
                binding.spinnerKuantitas.setText("16.000")
            }
            "DA 8294 PZ" ->{
                binding.kompartemen1.spinnerRMmIKompartemen.setText(0.305.toString())
                binding.kompartemen2.spinnerRMmIKompartemen.setText(0.34.toString())
                binding.spinnerKuantitas.setText("16.000")
            }
        }
    }

    private fun checkMainInput() {
        aft = binding.spinnerAft.text.toString()
        supplyPoint = binding.spinnerSupplyPoint.text.toString()
        transportir = binding.spinnerTransportir.text.toString()
        noPolisi = binding.spinnerNoPolisi.text.toString()
        kuantitas = binding.spinnerKuantitas.text.toString().replace(".","").toIntOrNull()
        hargaAvtur = binding.editHargaAvtur.text.toString().replace(".","").toDoubleOrNull()
        spvRsd = binding.spinnerSpvRsd.text.toString()
        sopirBridger1 = binding.editSopirBridger1.text.toString()
        sopirBridger2 = binding.editSopirBridger2.text.toString()
        note = binding.editCatatan.text.toString()



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
            sopir_bridger_2 = sopirBridger2,
            catatan = note
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

        if (updateData != null){
            val volumeControlToUpdate = AnalisaVolumeControlQuality(
                idAnalisa = updateData!!.analisaVolumeControl.idAnalisa,
                tanggal = tanggalBongkar,
                aft = aft,
                supply_point = supplyPoint,
                transportir = transportir,
                kuantitas = kuantitas,
                no_polisi = noPolisi,
                harga_avtur = hargaAvtur,
                spv_rsd = spvRsd,
                sopir_bridger_1 = sopirBridger1,
                sopir_bridger_2 = sopirBridger2,
                catatan = note
            )

            volumeViewModel.updateVolumeControlWithDetail(volumeControlToUpdate, detail)
            Toast.makeText(requireContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }else{
            volumeViewModel.insertVolumeWithDetail(volumeControl, detail)
            Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
        }



    }

    private fun setupUpdateData(data : AnalisaVolumeControlWithDetail){
        binding.buttonTangalBongkar.setText(TimeConverter.toReadableDate(data.analisaVolumeControl.tanggal ?: System.currentTimeMillis()))
        binding.spinnerAft.setText(data.analisaVolumeControl.aft ?: "")
        binding.spinnerSupplyPoint.setText(data.analisaVolumeControl.supply_point ?: "")
        binding.spinnerTransportir.setText(data.analisaVolumeControl.transportir ?: "")
        binding.spinnerNoPolisi.setText(data.analisaVolumeControl.no_polisi ?: "")
        binding.spinnerKuantitas.setText(data.analisaVolumeControl.kuantitas?.toString() ?: "")
        binding.editHargaAvtur.setText(data.analisaVolumeControl.harga_avtur?.toInt()?.toString() ?: "")
        binding.spinnerSpvRsd.setText(data.analisaVolumeControl.spv_rsd ?: "")
        binding.editSopirBridger1.setText(data.analisaVolumeControl.sopir_bridger_1 ?: "")
        binding.editSopirBridger2.setText(data.analisaVolumeControl.sopir_bridger_2 ?: "")
        binding.editCatatan.setText(data.analisaVolumeControl.catatan ?: "")

        var index = 0
        for (i in data.detailKompartemen){
            kompartemenView[index].lvKompartemen1.visibility = View.VISIBLE
            searcHolderView[index].cardViewResult.visibility = View.VISIBLE
            kompartemenView[index].spinnerKompartemen.setText(i.kompartemen ?: "")
            kompartemenView[index].editTeraKompartemen.setText(i.tera?.toString() ?: "")
            kompartemenView[index].editUkuranSuppyPointKompartemen.setText(i.ukuran_supply_point?.toString() ?: "")
            kompartemenView[index].spinnerRMmIKompartemen.setText(i.rmm_i?.toString() ?: "")
            kompartemenView[index].editUkuranDppuKompartemen.setText(i.ukuran_dppu?.toString() ?: "")
            kompartemenView[index].editDensityObsKompartemen.setText(i.density_obs?.toString() ?: "")
            kompartemenView[index].editTempObsKompartemen.setText(i.temp_obs?.toString() ?: "")
            index++
        }

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
        arrayRmmI[numerator]  = kompartemenHolder.spinnerRMmIKompartemen.text.toString().toDoubleOrNull()
        arrayUkuranDppu[numerator] = kompartemenHolder.editUkuranDppuKompartemen.text.toString().toIntOrNull()
        arrayDensityObs[numerator] = kompartemenHolder.editDensityObsKompartemen.text.toString().toDoubleOrNull()
        arrayTempObs[numerator] = kompartemenHolder.editTempObsKompartemen.text.toString().toIntOrNull()
        kuantitas = binding.spinnerKuantitas.text.toString().replace(".","").toIntOrNull()
        
        if (arrayKompartemen[numerator] != null && 
            (arrayTera[numerator] != null || arrayUkuranSupplyPoint[numerator] != null) &&
            arrayRmmI[numerator] != null && arrayUkuranDppu[numerator] != null &&
            arrayDensityObs[numerator] != null && arrayTempObs[numerator] != null && kuantitas != null) {
                arraySelisihUllage[numerator] = selisihUllage(arrayTera[numerator] ?: 0,
                    arrayUkuranSupplyPoint[numerator] ?: 0,
                    arrayUkuranDppu[numerator] ?: 0)

                arraySelisihLiter[numerator] = selisihLiter(arraySelisihUllage[numerator] ?: 0, arrayRmmI[numerator] ?: 0.0)
                submitDataViewModel(status, arrayDensityObs[numerator] ?: 0.0, arrayTempObs[numerator] ?: 0)
                kompartemenHolder.buttonNextKompartemen.isEnabled = true
                binding.buttonSaveData.isEnabled = true


        }else{
            Toast.makeText(requireContext(), "Mohon lengkapi data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSearch(holder :  CardViewHolderBinding,status: String){
        holder.imgResultIcon.visibility = View.GONE
        holder.tableViewResult.visibility = View.VISIBLE
        holder.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
        holder.row5Header.visibility = View.GONE
        holder.row5Body.visibility = View.GONE

        val border = if (kuantitas != null){
            kuantitas!! * 0.0015
        }else{
            0.0
        }

        val numerator = when{status == "Depan" -> 0; status == "Tengah" -> 1 else -> 2}

        holder.row1Header.text = "Selisih mm"
        val selisihMM : Int = arraySelisihUllage[numerator] ?: 0
        holder.row1Body.text = numberFormatter(selisihMM,4)

        holder.row2Header.text = "Selisih Liter"
        holder.row2Body.text = numberFormatter(arraySelisihLiter[numerator] ?: 0.0,4)

        holder.row3Header.text = "Density 15"
        holder.row3Body.text = numberFormatter(arrayDensity15[numerator],4)

        val literSelisih : Double = arraySelisihLiter[numerator] ?: 0.0
        val result = selisihLiter15(arrayCorrFactor[numerator],literSelisih)
        holder.row4Header.text = "Liter 15"
        holder.row4Body.text = numberFormatter(result,4)


        if (selisihMM <= -3) {
            holder.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
        }else{
            holder.row5Header.visibility = View.GONE
            holder.row5Body.visibility = View.GONE
            holder.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
        }
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


    override fun onResume() {
        super.onResume()
        val kompartemenAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, _kompartemenStatus)
        binding.kompartemen1.spinnerKompartemen.setAdapter(kompartemenAdapter)
        binding.kompartemen2.spinnerKompartemen.setAdapter(kompartemenAdapter)
        binding.kompartemen3.spinnerKompartemen.setAdapter(kompartemenAdapter)


        val arrayAft = arrayOf("AFT. Syamsudin Noor")
        val arraySupplyPoint = arrayOf("IT Banjarmasin")
        val arrayTransportir = arrayOf("PT. Pertamina Patra Niaga")
        val rMMArray = arrayOf(0.305,0.34,0.25,0.295,0.3)
        val arrayNoPolisiBridger = resources.getStringArray(R.array.bridger_choice)
        val arraySpvRsd = resources.getStringArray(R.array.spv_rsd_choice)
        val arrayQuantityBridger = resources.getStringArray(R.array.bridger_quantity_choice)


        val adapterAft = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, arrayAft)
        val adapterSupplyPoint = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, arraySupplyPoint)
        val adapterTransportir = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, arrayTransportir)
        val adapterRMM = ArrayAdapter(requireContext(),R.layout.spinner_item_holder,rMMArray)
        val adapterNoPolisiBridger = ArrayAdapter(requireContext(),R.layout.spinner_item_holder,arrayNoPolisiBridger)
        val adapterSpvRsd = ArrayAdapter(requireContext(),R.layout.spinner_item_holder,arraySpvRsd)
        val adapterQuantityBridger = ArrayAdapter(requireContext(),R.layout.spinner_item_holder,arrayQuantityBridger)

        binding.spinnerAft.setAdapter(adapterAft)
        binding.spinnerSupplyPoint.setAdapter(adapterSupplyPoint)
        binding.spinnerTransportir.setAdapter(adapterTransportir)
        binding.spinnerNoPolisi.setAdapter(adapterNoPolisiBridger)
        binding.spinnerKuantitas.setAdapter(adapterQuantityBridger)
        binding.spinnerSpvRsd.setAdapter(adapterSpvRsd)

        binding.kompartemen1.spinnerRMmIKompartemen.setAdapter(adapterRMM)
        binding.kompartemen2.spinnerRMmIKompartemen.setAdapter(adapterRMM)
        binding.kompartemen3.spinnerRMmIKompartemen.setAdapter(adapterRMM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(updateData : AnalisaVolumeControlWithDetail?) = VolumeControlFragment().apply {
            arguments = Bundle().apply {
                putParcelable("UPDATE_DATA", updateData)
            }
        }
    }


}


