package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment

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
import com.syamsudinnoor.aft.aviation.pertamina.adapter.ToppingUpAdapter
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FilterTimeBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentDataHolderToppingUpBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.activity.DataShowToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.DataToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.BridgerQualityPDFGenerator
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.dateDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.DialogHolder.timeDialog
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.showLoadingCustom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date


class DataHolderToppingUpFragment : Fragment() {

    private var _binding : FragmentDataHolderToppingUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : ToppingUpAdapter
    private lateinit var dataReport : List<ToppingUp>

    private val viewmodel : DataToppingUpViewModel by viewModels {
        val database = MainDatabase.getDatabase(requireContext())
        val repository = MainRepository(database.getDao())
        MainViewModelFactory(repository)
    }

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
        // Inflate the layout for this fragment
        _binding = FragmentDataHolderToppingUpBinding.inflate(inflater, container, false)
        setupRecyclerView()

        viewmodel.toppingUpList.observe(viewLifecycleOwner){
            adapter.submitList(it)
            dataReport = it
            if (it.isEmpty()){
                binding.buttonSave.isEnabled = false
                binding.buttonFilter.isEnabled = false
                binding.buttonSave.setBackgroundResource(android.R.color.darker_gray)
                binding.buttonFilter.setBackgroundResource(android.R.color.darker_gray)
                binding.recycleViewDataHolder.visibility = View.GONE
                binding.txtNoData.visibility = View.VISIBLE
            }else{
                binding.buttonSave.isEnabled = true
                binding.buttonFilter.isEnabled = true
                binding.buttonSave.setBackgroundResource(R.color.colorPrimary)
                binding.buttonFilter.setBackgroundResource(R.color.colorPrimary)
                binding.recycleViewDataHolder.visibility = View.VISIBLE
                binding.txtNoData.visibility = View.GONE
            }
        }

        binding.buttonFilter.setOnClickListener {
            settingDialogFilter()
        }

        binding.buttonSave.setOnClickListener {
            checkPermissionAndGeneratePdf()
        }

        binding.fabAdd.setOnClickListener {
            viewmodel.loadData()
            Toast.makeText(requireContext(), "Refreshing...", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    private fun startPdfGeneration() {
        // Ganti ini dengan data asli dari Room
        val sampleData = dataReport

        binding.buttonSave.isEnabled = false
        showLoadingCustom(viewLifecycleOwner,requireContext(),viewmodel.isLoading)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewmodel.statusLoading(true)
                val pdfUri = withContext(Dispatchers.IO) {
                    val pdfGenerator = ToppingUpPdfGenerator()
                    pdfGenerator.generatePdf(requireContext(), sampleData)
                }

                if (pdfUri != null) {
                    Toast.makeText(
                        requireContext(), "PDF berhasil disimpan di folder Download!", Toast.LENGTH_SHORT).show()
                    sharePdf(pdfUri)
                }
                binding.buttonSave.isEnabled = true
                viewmodel.statusLoading(false)
            }catch (e : Exception){
                Toast.makeText(requireContext(), "Gagal membuat PDF : ${e.message}.", Toast.LENGTH_SHORT).show()
                binding.buttonSave.isEnabled = true
                viewmodel.statusLoading(false)
            }finally {
                binding.buttonSave.isEnabled = true
                viewmodel.statusLoading(false)
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


    private fun setupRecyclerView() {
        adapter = ToppingUpAdapter({ it ->
            val intent = Intent(requireContext(), DataShowToppingUp::class.java)
            intent.putExtra(DataShowToppingUp.APP_NAME,it)
            startActivity(intent)
        }){
            settingDialog("Apakah anda yakin ingin menghapus data ini?",it)
        }
        binding.recycleViewDataHolder.adapter = adapter
        binding.recycleViewDataHolder.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun settingDialog(massage: String,dataReport : ToppingUp){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.costum_delete_quality_control)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val titleText : TextView = dialog.findViewById(R.id.txt_title)
        titleText.text = getString(R.string.hapus_data, dataReport.totalisator_akhir?.uppercase())
        val massageText  : TextView = dialog.findViewById(R.id.txt_massage)
        massageText.text = massage


        val yesButton : Button = dialog.findViewById(R.id.btn_yes)
        yesButton.setOnClickListener {
            viewmodel.delete(dataReport.id)
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
                localBinding.buttonDate.text = "$day:$month:$year"
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
                    viewmodel.filterDataByDate(startTime,endTime)
                    Toast.makeText(requireContext(),"Data berhasil di Update", Toast.LENGTH_SHORT).show()
                }

            }else if (startHour.isNotEmpty() && startMinute.isNotEmpty() && endHour.isNotEmpty() && endMinute.isNotEmpty()) {
                val date = Date()
                val startTime = TimeConverter.concatenateStringDate(date,startHour,startMinute)
                val endTime = TimeConverter.concatenateStringDate(date,endHour,endMinute)
                viewmodel.filterDataByDate(startTime,endTime)
                Toast.makeText(requireContext(),"Data berhasil di Update", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Argumen Tidak Valid", Toast.LENGTH_SHORT).show()

            }
            dialog.dismiss()
        }

        dialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}