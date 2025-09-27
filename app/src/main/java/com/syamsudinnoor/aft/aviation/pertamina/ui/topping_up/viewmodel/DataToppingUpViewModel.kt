package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import kotlinx.coroutines.launch

class DataToppingUpViewModel(private val repository: MainRepository) : ViewModel(){

    private val _toppingUpList = MutableLiveData<List<ToppingUp>>()
    val toppingUpList: LiveData<List<ToppingUp>> = _toppingUpList

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getToppingUp().collect { allData ->
                _toppingUpList.value = allData
            }
        }
    }

    fun insert(toppingUp: ToppingUp) {
        viewModelScope.launch {
            repository.insertToppingUp(toppingUp)
        }
    }

    fun delete(id : Int){
        viewModelScope.launch {
            repository.deleteToppingUp(id)
        }
    }

    fun filterDataByDate(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            repository.getToppingUpByDate(startTime, endTime).collect { filteredData ->
                _toppingUpList.value = filteredData
            }
        }
    }

    fun updateToppingUp(toppingUp: ToppingUp) {
        viewModelScope.launch {
            repository.updateToppingUp(toppingUp)
        }
    }

}