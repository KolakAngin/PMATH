package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Density_15
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_20
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_21
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_22
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository


class ToppingUpViewModel(private val repository: SNoorRepository) : ViewModel() {

    private val tangki9Mm = MutableLiveData<Double>()
    val tangki9Result: LiveData<Tangki_9?> = tangki9Mm.switchMap { mm ->
        repository.getResultTangki9(mm)
    }
    fun searchTangki9(mm: Double) {
        tangki9Mm.value = mm
    }

    private val tangki10Mm = MutableLiveData<Double>()
    val tangki10Result: LiveData<Tangki_10?> = tangki10Mm.switchMap { mm ->
        repository.getResultTangki10(mm)
    }
    fun searchTangki10(mm: Double) {
        tangki10Mm.value = mm
    }

    private val _snr20 = MutableLiveData<Double>()

    val snr20: LiveData<Snr_20?> = _snr20.switchMap { mm ->
        repository.getResultSnr20(mm)
    }

    private val _snr21 = MutableLiveData<Double>()

    val snr21: LiveData<Snr_21?> = _snr21.switchMap { mm ->
        repository.getResultSnr21(mm)
    }

    private val _snr22 = MutableLiveData<Double>()
    val snr22 : LiveData<Snr_22?> = _snr22.switchMap { mm ->
        repository.getResultSnr22(mm)
    }

    fun getSnr20(mm: Double) {
        _snr20.value = mm

    }

    fun getSnr21(mm: Double) {
        _snr21.value = mm
    }

    fun  getSnr22(mm: Double) {
        _snr22.value = mm
    }


    private val _isRefeullerSelected = MutableLiveData<Boolean>()
    val isRefeullerSelected: LiveData<Boolean>
        get() = _isRefeullerSelected

    private val _isSessionSelected = MutableLiveData<Boolean>()
    val isSessionSelected: LiveData<Boolean>
        get() = _isSessionSelected


    private val _isMMValid = MutableLiveData<Boolean>()
    val isMMValid: LiveData<Boolean>
        get() = _isMMValid

    private val _isValid = MediatorLiveData<Boolean>()
    val isValid: LiveData<Boolean>
        get() = _isValid



    init {
        _isValid.addSource(_isRefeullerSelected) { validateForm() }
        _isValid.addSource(_isSessionSelected) { validateForm() }
        _isValid.addSource(_isMMValid) { validateForm() }
    }

    private fun validateForm() {
        val tankSelected = _isRefeullerSelected.value ?: false
        val sessionSelected = _isSessionSelected.value ?: false
        val isMMValid = _isMMValid.value ?: false

        _isValid.value = tankSelected && sessionSelected && isMMValid
    }

    fun onRefeullerSelected(isSelected: Boolean) {
        _isRefeullerSelected.value = isSelected
    }

    fun onSessionSelected(isSelected: Boolean) {
        _isSessionSelected.value = isSelected
    }

    fun onMMValid(isValid: Boolean) {
        _isMMValid.value = isValid
    }

}