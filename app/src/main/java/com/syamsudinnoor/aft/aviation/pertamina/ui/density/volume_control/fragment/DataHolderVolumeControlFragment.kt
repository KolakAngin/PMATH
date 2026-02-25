package com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.fragment

import ToppingUpPdfGenerator
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.adapter.VolumeControlAdapter
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FilterTimeBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentDataHolderVolumeControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlMainViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.activity_holder.DataShowActivity
import com.syamsudinnoor.aft.aviation.pertamina.utility.AnalisaVolumeControlPdfGenerator
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.dateDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter
import com.syamsudinnoor.aft.aviation.pertamina.utility.showLoadingCustom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date


class DataHolderVolumeControlFragment : Fragment() {

    private var _binding : FragmentDataHolderVolumeControlBinding? = null
    private val binding get() = _binding!!

    private val viewModel : VolumeControlMainViewModel by viewModels{
        val database = MainDatabase.getDatabase(requireContext())
        val dao = MainRepository(database.getDao())
        MainViewModelFactory(dao)
    }

    private lateinit var adapter: VolumeControlAdapter
    private lateinit var dataReport : List<AnalisaVolumeControlWithDetail>

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted : Boolean ->
        if (isGranted){
            startPdfGeneration()
        }else{
            Toast.makeText(requireContext(), "Izin penyimpanan dibutuhkan untuk menyimpan PDF.", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataHolderVolumeControlBinding.inflate(inflater, container, false)
        adapter = VolumeControlAdapter({
            val intent = Intent(requireContext(), DataShowActivity::class.java)
            intent.putExtra("DATA",it)
            startActivity(intent)

        }){
            settingDialog("Apakah anda yakin ingin menghapus data ini?", it)

        }
        binding.recycleViewDataHolder.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewDataHolder.adapter = adapter


        viewModel.allVolumeControlWithDetail.observe(viewLifecycleOwner){
            adapter.submitList(it)
            //modelingAnalyticsData(it)
            dataReport = it
            if (it.isEmpty()){
                binding.buttonSave.isEnabled = false
                binding.buttonSave.setBackgroundResource(android.R.color.darker_gray)
                binding.recycleViewDataHolder.visibility = View.GONE
                binding.txtNoData.visibility = View.VISIBLE
            }else{
                binding.buttonSave.isEnabled = true
                binding.buttonSave.setBackgroundResource(R.color.colorPrimary)
                binding.recycleViewDataHolder.visibility = View.VISIBLE
                binding.txtNoData.visibility = View.GONE
            }
        }
        binding.fabAdd.setOnClickListener {
            val startTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 1)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endTime = Calendar.getInstance().timeInMillis
            viewModel.selectFilterVolumeData(startTime,endTime)
            Toast.makeText(requireContext(), "Refreshing...", Toast.LENGTH_SHORT).show()
        }

        binding.buttonFilter.setOnClickListener {
            settingDialogFilter()
        }

        binding.buttonSave.setOnClickListener {
            checkPermissionAndGeneratePdf()
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun startPdfGeneration() {
        val sampleData = dataReport

        binding.buttonSave.isEnabled = false
        showLoadingCustom(viewLifecycleOwner,requireContext(),viewModel.isLoading)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.loadingStatus(true)
                val pdfToUri = withContext(Dispatchers.IO){
                    val pdfGenerator = AnalisaVolumeControlPdfGenerator()
                    pdfGenerator.generatePdf(requireContext(), sampleData)
                }
                if (pdfToUri != null) {
                    Toast.makeText(requireContext(), "PDF berhasil disimpan di folder Download!", Toast.LENGTH_SHORT).show()
                    sharePdf(pdfToUri)
                }
                binding.buttonSave.isEnabled = true
                viewModel.loadingStatus(false)
            }catch (e : Exception){
                Toast.makeText(requireContext(), "Gagal membuat PDF : ${e.message}.", Toast.LENGTH_SHORT).show()
                binding.buttonSave.isEnabled = true
                viewModel.loadingStatus(false)
            }finally {
                binding.buttonSave.isEnabled = true
                viewModel.loadingStatus(false)
                false
            }
        }
    }

    // FUNGSI BARU untuk share
    private fun sharePdf(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // Menampilkan chooser agar pengguna bisa memilih aplikasi
        startActivity(Intent.createChooser(shareIntent, "Bagikan PDF melalui..."))
    }

    private fun checkPermissionAndGeneratePdf() {
        // Hanya perlu cek permission untuk Android 9 ke bawah
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Izin sudah ada
                    startPdfGeneration()
                }
                else -> {
                    // Minta izin
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        } else {
            // Untuk Android 10+, tidak perlu permission khusus untuk menyimpan ke Download
            startPdfGeneration()
        }
    }



    private fun settingDialog(massage: String,dataReport : AnalisaVolumeControlWithDetail){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.costum_delete_quality_control)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val titleText : TextView = dialog.findViewById(R.id.txt_title)
        titleText.text = getString(R.string.hapus_data, dataReport.analisaVolumeControl.no_polisi?.uppercase())
        val massageText  : TextView = dialog.findViewById(R.id.txt_massage)
        massageText.text = massage


        val yesButton : Button = dialog.findViewById(R.id.btn_yes)
        yesButton.setOnClickListener {
            viewModel.deleteVolumeData(dataReport.analisaVolumeControl)
            Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val noButton : Button = dialog.findViewById(R.id.btn_no)
        noButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun settingDialogFilter(){
        val localBinding = FilterTimeBinding.inflate(layoutInflater)
        var year : String = ""
        var month : String = ""
        var day : String = ""
        var startHour : String = ""
        var startMinute : String = ""
        var endHour : String = ""
        var endMinute : String = ""

        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(localBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        localBinding.buttonDate.setOnClickListener {
            dateDialog(requireContext()){ yearDialog, monthDialog, dayOfMonthDialog ->
                year = yearDialog.toString()
                month = (monthDialog + 1).toString()
                day = dayOfMonthDialog.toString()

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, yearDialog)
                    set(Calendar.MONTH, monthDialog)
                    set(Calendar.DAY_OF_MONTH, dayOfMonthDialog)
                }
                localBinding.buttonDate.text = TimeConverter.toReadableDate(calendar.timeInMillis)
            }
        }


        localBinding.buttonTimeFrom.setOnClickListener {
            timeDialog(requireContext(),"Pilih Jam Mulai"){ hour, minute ->
                startHour = hour.toString()
                startMinute = minute.toString()
                localBinding.buttonTimeFrom.text = "$startHour:$startMinute"

            }
        }

        localBinding.buttonTimeEnd.setOnClickListener {
            timeDialog(requireContext(),"Pilih Jam Berakhir"){ hour, minute ->
                endHour = hour.toString()
                endMinute = minute.toString()
                localBinding.buttonTimeEnd.text = "$endHour:$endMinute"

            }
        }

        localBinding.buttonFilter.setOnClickListener {

            if (year.isNotEmpty() && month.isNotEmpty() && day.isNotEmpty()
                &&  startHour.isNotEmpty() && startMinute.isNotEmpty()
                && endHour.isNotEmpty() && endMinute.isNotEmpty()){
                val date : Date? = TimeConverter.parseStringDate("$day:$month:$year")

                if (date != null){
                    val startTime = TimeConverter.concatenateStringDate(date,startHour,startMinute)
                    val endTime = TimeConverter.concatenateStringDate(date,endHour,endMinute)
                    viewModel.selectFilterVolumeData(startTime,endTime)
                    Toast.makeText(requireContext(),"Data berhasil di Update", Toast.LENGTH_SHORT).show()
                }

            }else if (startHour.isNotEmpty() && startMinute.isNotEmpty() && endHour.isNotEmpty() && endMinute.isNotEmpty()) {
                val date = Date()
                val startTime = TimeConverter.concatenateStringDate(date,startHour,startMinute)
                val endTime = TimeConverter.concatenateStringDate(date,endHour,endMinute)
                viewModel.selectFilterVolumeData(startTime,endTime)
                Toast.makeText(requireContext(),"Data berhasil di Update", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Argumen Tidak Valid", Toast.LENGTH_SHORT).show()

            }
            dialog.dismiss()
        }

        dialog.show()

    }

//    private fun modelingAnalyticsData(data : List<AnalisaVolumeControlWithDetail>){
//
//        var sumOfLost : Double = 0.0
//        val totalBridger : Int = data.size
//
//        for(i in data){
//            sumOfLost += i.detailKompartemen.sumOf { it.liter_15 ?: 0.0 }
//        }
//
//        binding.txtJumlahBridger.text = totalBridger.toString()ƒ
//        if (sumOfLost >= 0){
//            binding.imgPlusTotal.setImageResource(R.drawable.up_arrow)
//        }else{
//            binding.imgPlusTotal.setImageResource(R.drawable.down_arrow)
//        }
//
//        binding.txtPlusTotal.text = numberFormatter(sumOfLost)
//
//    }

    override fun onStart() {
        super.onStart()
        val startTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endTime = Calendar.getInstance().timeInMillis
        viewModel.selectFilterVolumeData(startTime,endTime)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}


