package com.syamsudinnoor.aft.aviation.pertamina.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: SNoorRepository) : ViewModel() {


    private val _loginSuccess = MutableLiveData<Pair<String, String>>()
    val logginSucces : LiveData<Boolean> = _loginSuccess.switchMap { params ->
        repository.getUserPMATH(params.first, params.second)
    }


    fun loginUser(username: String, password: String) {
        Log.d("LoginViewModel", "logginSucces: saya dijalnakan di login user")
        _loginSuccess.value = Pair(username, password)
    }

}

