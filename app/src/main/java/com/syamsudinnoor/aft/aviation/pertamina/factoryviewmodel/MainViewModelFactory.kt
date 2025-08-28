package com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.QualityControlViewModel

class MainViewModelFactory(val repository: MainRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QualityControlViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return QualityControlViewModel(repository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}