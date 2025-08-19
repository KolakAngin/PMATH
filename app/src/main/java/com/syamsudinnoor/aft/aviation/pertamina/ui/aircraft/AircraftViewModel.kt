package com.syamsudinnoor.aft.aviation.pertamina.ui.aircraft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AircraftViewModel : ViewModel() {


    private val _isRemain = MutableLiveData<Boolean>()
    val isRemain: LiveData<Boolean>
        get() = _isRemain

    private val _isFinal = MutableLiveData<Boolean>()
    val isFinal: LiveData<Boolean>
        get() = _isFinal

    private val _isRemainRefeuller = MutableLiveData<Boolean>()
    val isRemainRefeuller: LiveData<Boolean>
        get() = _isRemain

    private val _isValid = MediatorLiveData<Boolean>()
    val isValid: LiveData<Boolean>
        get() = _isValid

    init {
        _isValid.addSource(_isRemain) { validateForm() }
        _isValid.addSource(_isFinal) { validateForm() }
        _isValid.addSource(_isRemainRefeuller) { validateForm() }
    }

    private fun validateForm() {
        val remainValid = _isRemain.value ?: false
        val finalValid = _isFinal.value ?: false
        val remainRefeullerValid = _isRemainRefeuller.value ?: false

        _isValid.value = remainValid && finalValid && remainRefeullerValid
    }

    fun onRemainInputChanged(isValid: Boolean) {
        _isRemain.value = isValid
    }

    fun onFinalInputChanged(isValid: Boolean) {
        _isFinal.value = isValid

    }

    fun onRemainRefeullerChanged(isValid: Boolean) {
        _isRemainRefeuller.value = isValid
    }

}