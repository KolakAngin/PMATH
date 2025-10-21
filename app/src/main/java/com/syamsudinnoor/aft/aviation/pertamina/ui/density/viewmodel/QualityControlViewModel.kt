package com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import kotlinx.coroutines.launch



class QualityControlViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _qualityControlList = MutableLiveData<List<BridgerQualityControl>>()
    val qualityControlList: LiveData<List<BridgerQualityControl>> = _qualityControlList

    init {
        loadAllData()
    }

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading

    fun statusLoading(status : Boolean){
        _isLoading.value = status
    }

    fun loadAllData() {
        Log.d("myDebug","Masuk loadData")
        viewModelScope.launch {
            mainRepository.getBridgerQualityControl().collect { allData ->
                _qualityControlList.value = allData
            }
        }
        Log.d("myDebug","Selesai loadData")
    }

    fun filterDataByDate(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            mainRepository.getBridgerQualityControlByDate(startTime, endTime).collect { filteredData ->
                _qualityControlList.value = filteredData
            }
        }
    }

    fun insert(bridgerQualityControl: BridgerQualityControl) {
        viewModelScope.launch {
            mainRepository.insertBridgerQualityControl(bridgerQualityControl)
        }
    }

    fun delete(id : Int){
        viewModelScope.launch {
            mainRepository.deleteBridgerQualityControl(id)
        }
    }

    fun update(bridgerQualityControl: BridgerQualityControl){
        viewModelScope.launch {
            mainRepository.updateBridgerQuality(bridgerQualityControl)
        }
    }


}
