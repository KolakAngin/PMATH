package com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_11
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository

class DippingTankViewModel(private val repository: SNoorRepository) : ViewModel() {
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

    private val tangki11Mm = MutableLiveData<Double>()
    val tangki11Result: LiveData<Tangki_11?> = tangki11Mm.switchMap { mm ->
        repository.getResultTangki11(mm)
    }
    fun searchRangki11(mm: Double){
        tangki11Mm.value = mm
    }

    private val _isTankSelected = MutableLiveData<Boolean>()
    val isTankSelected: LiveData<Boolean> = _isTankSelected

    private val _isSessionSelected = MutableLiveData<Boolean>()
    val isSessionSelected: LiveData<Boolean> = _isSessionSelected

    private val _isMmValid = MutableLiveData<Boolean>()
    val isMmValid: LiveData<Boolean> = _isMmValid


    private val _isAllItemValid = MediatorLiveData<Boolean>()
    val isAllItemValid: LiveData<Boolean> = _isAllItemValid

    init {
        _isAllItemValid.addSource(_isTankSelected) {
            validateForm()
        }
        _isAllItemValid.addSource(_isSessionSelected) {
            validateForm()
        }

        _isAllItemValid.addSource(_isMmValid) {
            validateForm()
        }
    }

    fun onTankSelected(isSelected: Boolean) {
        _isTankSelected.value = isSelected
    }

    fun onSessionSelected(isSelected: Boolean) {
        _isSessionSelected.value = isSelected
    }


    fun onMMInputValid(isValid: Boolean) {
        _isMmValid.value = isValid
    }

    private fun validateForm() {
        val tankSelected = _isTankSelected.value ?: false
        val sessionSelected = _isSessionSelected.value ?: false
        val mmValid = _isMmValid.value ?: false

        _isAllItemValid.value = tankSelected && sessionSelected && mmValid
    }






}