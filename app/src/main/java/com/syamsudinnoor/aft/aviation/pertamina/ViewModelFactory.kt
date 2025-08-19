package com.syamsudinnoor.aft.aviation.pertamina

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.DensityViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.DippingTankViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.ToppingUpViewModel

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
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}