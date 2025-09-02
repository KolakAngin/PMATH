package com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.QualityControlViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlMainViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.DataToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel

class MainViewModelFactory(val repository: MainRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QualityControlViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return QualityControlViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(DataToppingUpViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DataToppingUpViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(VolumeControlMainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return VolumeControlMainViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}