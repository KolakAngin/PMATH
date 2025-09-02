package com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.CorrFactor
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository

class VolumeControlViewModel(private val repository: SNoorRepository) : ViewModel() {

    private val _corrFactorDepan = MutableLiveData<String>()
    val corrFactorDepan : LiveData<CorrFactor?> = _corrFactorDepan.switchMap {densityCorr ->
        repository.getResultCorrFactor(densityCorr)
    }

    fun getCorrFactorDepan(densityCorr : String){
        _corrFactorDepan.value = densityCorr
    }

    private val _corrFactorTengah = MutableLiveData<String>()
    val corrFactorTengah : LiveData<CorrFactor?> = _corrFactorTengah.switchMap {densityCorr ->
        repository.getResultCorrFactor(densityCorr)
    }

    fun getCorrFactorTengah(densityCorr : String){
        _corrFactorTengah.value = densityCorr
    }

    private val _corrFactorBelakang = MutableLiveData<String>()
    val corrFactorBelakang : LiveData<CorrFactor?> = _corrFactorBelakang.switchMap {densityCorr ->
        repository.getResultCorrFactor(densityCorr)
    }

    fun getCorrFactorBelakang(densityCorr : String){
        _corrFactorBelakang.value = densityCorr

    }
}