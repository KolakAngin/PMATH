package com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalyticsDataVolumeControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.SumOfQualityControlData
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class DateRange(
    val start: Long,
    val end: Long
)
class VolumeControlMainViewModel(private val repository: MainRepository) : ViewModel() {

    // ----------------- WAKTU DEFAULT -----------------
    fun setStartTime(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun setEndTime(): Long = Calendar.getInstance().timeInMillis


    // ----------------- LOADING STATUS -----------------
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    fun loadingStatus(isLoading: Boolean) {
        _isLoading.value = isLoading
    }


    // ----------------- TRIGGER FILTER -----------------
    private val _startTime = MutableLiveData<Long>()
    private val _endTime = MutableLiveData<Long>()

    // ----------------- DATA UTAMA -----------------
    val allVolumeControlWithDetail: LiveData<List<AnalisaVolumeControlWithDetail>> =
        _startTime.switchMap { start ->
            _endTime.switchMap { end ->
                repository.getAnalisaVolumeControlByDate(start, end).asLiveData()
            }
        }

    init {
        // default: tampilkan data hari ini
        _startTime.value = setStartTime()
        _endTime.value = setEndTime()
    }


    // ----------------- MEMUAT DATA SECARA EXPLISIT (JIKA DIPERLUKAN) -----------------
    fun loadAllData() {
        // hanya mengganti waktu agar LiveData switchMap update
        _startTime.value = setStartTime()
        _endTime.value = setEndTime()
    }


    // ----------------- INSERT DATA -----------------
    fun insertVolumeWithDetail(
        volumeControl: AnalisaVolumeControlQuality,
        detail: List<DetailKompartemen>
    ) {
        viewModelScope.launch {
            loadingStatus(true)
            repository.insertAllAnalisaVolumeControlWithDetail(volumeControl, detail)
            loadingStatus(false)
        }
        // jangan collect ulang, cukup trigger ulang filter waktu
        _endTime.value = setEndTime()
    }


    // ----------------- DELETE DATA -----------------
    fun deleteVolumeData(analisaVolumeControl: AnalisaVolumeControlQuality) {
        viewModelScope.launch {
            loadingStatus(true)
            repository.deleteSelectedDataVolume(analisaVolumeControl)
            loadingStatus(false)
        }
    }


    // ----------------- FILTER TANGGAL EXPLISIT -----------------
    fun selectFilterVolumeData(startTime: Long, endTime: Long) {
        _startTime.value = startTime
        _endTime.value = endTime
    }


    // ----------------- UPDATE DATA -----------------
    fun updateVolumeControlWithDetail(
        analisaVolume: AnalisaVolumeControlQuality,
        detail: List<DetailKompartemen>
    ) {
        viewModelScope.launch {
            loadingStatus(true)
            repository.updateVolumeControl(analisaVolume, detail)
            loadingStatus(false)
        }
    }


    // ----------------- ANALYTICS DATA -----------------
    private val _analyticsVolumeControl = MutableLiveData<List<AnalyticsDataVolumeControl>>()
    val analyticsVolumeControl: LiveData<List<AnalyticsDataVolumeControl>> =
        _analyticsVolumeControl

    fun getAnalyticsVolumeControl(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            repository.getAnalyticsVolumeControl(startTime, endTime).collect {
                _analyticsVolumeControl.value = it
            }
        }
    }
}

