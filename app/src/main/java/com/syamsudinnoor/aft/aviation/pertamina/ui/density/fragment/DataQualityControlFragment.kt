package com.syamsudinnoor.aft.aviation.pertamina.ui.density.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.adapter.BridgerQualityAdapter
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FilterTimeBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentDataQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.MainViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.BridgerDataShowActivty
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.QualityControlViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DataQualityControlFragment : Fragment() {

    private var _binding : FragmentDataQualityControlBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel : QualityControlViewModel by viewModels {
        val database = MainDatabase.getDatabase(requireContext())
        val repository = MainRepository(database.getDao())
        MainViewModelFactory(repository)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmentQ
        _binding = FragmentDataQualityControlBinding.inflate(inflater, container, false)

        val adapter = BridgerQualityAdapter({ it ->
            val intent = Intent(requireContext(), BridgerDataShowActivty::class.java)
            intent.putExtra(BridgerDataShowActivty.APP_NAME, it)
            startActivity(intent)
        }){
            settingDialog("Apakah anda yakin ingin menghapus data ini?",it)
        }
        binding.recycleViewDataHolder.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewDataHolder.adapter = adapter
        mainViewModel.qualityControlList.observe(viewLifecycleOwner){
            adapter.submitList(it)
            if (it.isEmpty()){
                binding.recycleViewDataHolder.visibility = View.GONE
                binding.txtNoData.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Tidak Ada Data", Toast.LENGTH_SHORT).show()
            }else{
                binding.recycleViewDataHolder.visibility = View.VISIBLE
                binding.txtNoData.visibility = View.GONE
            }
        }

        binding.buttonFilter.setOnClickListener {
            settingDialogFilter()
        }

        return binding.root
    }


    private fun settingDialog(massage: String,dataReport : BridgerQualityControl){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.costum_delete_quality_control)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val titleText : TextView = dialog.findViewById(R.id.txt_title)
        titleText.text = "Hapus Data : ${dataReport.seal?.uppercase()}"
        val massageText  : TextView= dialog.findViewById(R.id.txt_massage)
        massageText.text = massage


        val yesButton : TextView = dialog.findViewById(R.id.btn_yes)
        yesButton.setOnClickListener {
            mainViewModel.delete(dataReport.id)
            Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val noButton : TextView = dialog.findViewById(R.id.btn_no)
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


        localBinding.buttonFilter.setOnClickListener {
            year = localBinding.edYyStart.text.toString()
            month = localBinding.edMmStart.text.toString()
            day = localBinding.edDdStart.text.toString()

            startHour = localBinding.edHStart.text.toString()
            startMinute = localBinding.edMStart.text.toString()

            endHour = localBinding.edHEnd.text.toString()
            endMinute = localBinding.edMEnd.text.toString()

            if (year.isNotEmpty() && month.isNotEmpty() && day.isNotEmpty()
                &&  startHour.isNotEmpty() && startMinute.isNotEmpty()
                && endHour.isNotEmpty() && endMinute.isNotEmpty()){
                val date : Date? = TimeConverter.parseStringDate("$day:$month:$year")

                if (date != null){
                    val startTime = TimeConverter.concatenateStringDate(date,startHour,startMinute)
                    val endTime = TimeConverter.concatenateStringDate(date,endHour,endMinute)
                    mainViewModel.filterDataByDate(startTime,endTime)
                    Toast.makeText(requireContext(),"Data berhasil du Update", Toast.LENGTH_SHORT).show()
                }

            }else if (startHour.isNotEmpty() && startMinute.isNotEmpty() && endHour.isNotEmpty() && endMinute.isNotEmpty()) {
                val date = Date()
                val startTime = TimeConverter.concatenateStringDate(date,startHour,startMinute)
                val endTime = TimeConverter.concatenateStringDate(date,endHour,endMinute)
                mainViewModel.filterDataByDate(startTime,endTime)
                Toast.makeText(requireContext(),"Data berhasil du Update", Toast.LENGTH_SHORT).show()
            }else{
                    Toast.makeText(requireContext(), "Argumen Tidak Valid", Toast.LENGTH_SHORT).show()

            }
            dialog.dismiss()
        }

        dialog.show()

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}