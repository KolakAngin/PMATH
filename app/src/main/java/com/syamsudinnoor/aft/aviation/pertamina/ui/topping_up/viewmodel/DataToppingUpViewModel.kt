package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.MainRepository
import kotlinx.coroutines.launch
import java.util.Calendar


class DataToppingUpViewModel(private val repository: MainRepository) : ViewModel(){


    val cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val startTime = cal.timeInMillis
    val endTime = Calendar.getInstance().timeInMillis


    private val _toppingUpList = MutableLiveData<List<ToppingUp>>()
    val toppingUpList: LiveData<List<ToppingUp>> = _toppingUpList

    init {
        loadData()
        Log.d("Data Topping Up", "init $startTime : $endTime")
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getToppingUpByDate(startTime,endTime).collect { allData ->
                _toppingUpList.value = allData
            }
        }
    }

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading

    fun statusLoading(status : Boolean){
        _isLoading.value = status
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