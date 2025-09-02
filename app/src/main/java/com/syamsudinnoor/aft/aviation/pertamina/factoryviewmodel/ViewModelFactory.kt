package com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.DensityViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.VolumeControlViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.DippingTankViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.main.viewmodel.LoginViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel

class ViewModelFactory(private val repository: SNoorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DensityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DensityViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ToppingUpViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ToppingUpViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(DippingTankViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DippingTankViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(VolumeControlViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolumeControlViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        else throw IllegalArgumentException("Unknown ViewModel class")
    }
}