package com.syamsudinnoor.aft.aviation.pertamina.ui.density

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Density_15
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository

class DensityViewModel(private val repository: SNoorRepository) : ViewModel() {

    // --- Untuk Kueri Density ---
    private val densityParams = MutableLiveData<Pair<Double, Double>>()
    val densityResult: LiveData<Density_15?> = densityParams.switchMap { params ->
        repository.getResultDensity(params.first, params.second)
    }
    fun searchDensity(density: Double, temperature: Double) {
        densityParams.value = Pair(density, temperature)
    }

    // Gunakan underscore untuk MutableLiveData privat
    private val _isDensityValid = MutableLiveData<Boolean>()
    val isDensityValid: LiveData<Boolean> = _isDensityValid

    private val _isTemperatureValid = MutableLiveData<Boolean>()
    val isTemperatureValid: LiveData<Boolean> = _isTemperatureValid

    // Inilah "Manajer" kita: MediatorLiveData untuk hasil akhir
    private val _isFormValid = MediatorLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> = _isFormValid

    init {
        // Tambahkan sumber yang ingin diawasi
        _isFormValid.addSource(_isDensityValid) {
            validateForm() // Panggil validasi setiap kali density berubah
        }
        _isFormValid.addSource(_isTemperatureValid) {
            validateForm() // Panggil validasi setiap kali temperatur berubah
        }
    }

    // Fungsi untuk mengubah nilai dari input
    fun onDensityInputChanged(isValid: Boolean) {
        _isDensityValid.value = isValid
    }

    fun onTemperatureInputChanged(isValid: Boolean) {
        _isTemperatureValid.value = isValid
    }

    // Fungsi validasi terpusat
    private fun validateForm() {
        val densityValid = _isDensityValid.value ?: false
        val tempValid = _isTemperatureValid.value ?: false

        // Atur nilai MediatorLiveData berdasarkan gabungan kondisi
        _isFormValid.value = densityValid && tempValid
    }

}