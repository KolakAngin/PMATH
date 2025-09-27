package com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VolumeControlMainViewModel(private val repository: MainRepository) : ViewModel() {


    private val _allVolumeControlWithDetail = MutableLiveData<List<AnalisaVolumeControlWithDetail>>()
    val allVolumeControlWithDetail : LiveData<List<AnalisaVolumeControlWithDetail>> =  _allVolumeControlWithDetail

    init {
        loadAllData()
    }
    fun loadAllData(){
        viewModelScope.launch {
            repository.getAllDataVolumeControl().collect { allData ->
                _allVolumeControlWithDetail.value = allData
            }
        }
    }


    fun insertVolumeWithDetail(volumeControl : AnalisaVolumeControlQuality, detail : List<DetailKompartemen>){
        viewModelScope.launch {
            repository.insertAllAnalisaVolumeControlWithDetail(volumeControl, detail)
        }
    }

    fun deleteVolumeData(analisaVolumeControl: AnalisaVolumeControlQuality){
        viewModelScope.launch {
            repository.deleteSelectedDataVolume(analisaVolumeControl)
        }
    }

    fun selectFilterVolumeData(startTime : Long, endTime : Long){
        viewModelScope.launch {
            repository.getAnalisaVolumeControlByDate(startTime, endTime).collect{
                _allVolumeControlWithDetail.value = it
            }
        }
    }

    fun updateVolumeControlWithDetail(analisaVolume : AnalisaVolumeControlQuality, detail : List<DetailKompartemen>){
        viewModelScope.launch {
            repository.updateVolumeControl(analisaVolume,detail)
        }

    }
}