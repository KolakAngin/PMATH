package com.syamsudinnoor.aft.aviation.pertamina.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainLoginViewModel(application: Application) : AndroidViewModel(application) {

    // State untuk mengontrol durasi splash screen
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // State untuk hasil pengecekan sesi
    private val _isSessionValid = MutableStateFlow<Boolean?>(null)
    val isSessionValid = _isSessionValid.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // Beri sedikit jeda agar splash screen terlihat
//            delay(1500)

            _isSessionValid.value = isUserSessionValid()
            _isLoading.value = false // Selesai cek, sembunyikan splash screen
        }
    }

    // Logika pengecekan sesi 1 jam
    private fun isUserSessionValid(): Boolean {
        val sharedPref = getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false)
        val loginTimestamp = sharedPref.getLong("LOGIN_TIMESTAMP", 0L)

        if (!isLoggedIn || loginTimestamp == 0L) return false

        val currentTime = System.currentTimeMillis()
        val oneHourInMillis = 3600 * 1000

        if ((currentTime - loginTimestamp) > oneHourInMillis) {
            // Sesi kedaluwarsa, hapus data login
            with(sharedPref.edit()) {
                clear()
                apply()
            }
            return false
        }
        return true
    }
}