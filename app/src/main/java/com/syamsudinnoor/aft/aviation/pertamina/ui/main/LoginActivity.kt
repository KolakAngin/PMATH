package com.syamsudinnoor.aft.aviation.pertamina.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityLoginBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.main.viewmodel.LoginViewModel
import androidx.core.content.edit
import com.syamsudinnoor.aft.aviation.pertamina.ui.flashlight.FlashLightActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels {
        val repository = SNoorRepository(SnoorRoomDatabase.getDatabase(this).sNoorDao())
        ViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLogin()
        //observeLoading()
        binding.loginButton.setOnClickListener {
            val userName = binding.username.text.toString().trim()
            val passWord = binding.password.text.toString().trim()
            if(userName.isNotEmpty() && passWord.isNotEmpty()){
                viewModel.loginUser(userName,passWord)
            }else{
                Toast.makeText(this, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sosButton.setOnClickListener {
            val intent = Intent(this, FlashLightActivity::class.java)
            intent.putExtra("SOS", true)
            startActivity(intent)
        }

    }

    private fun observeLogin(){
        viewModel.logginSucces.observe(this){
            Log.d("LoginActivity", "observeLogin: $it")
            when(it){
                true -> {
                    saveLoginStatus(true)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                false -> {
                    Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }
    }
    private fun saveLoginStatus(isLogin : Boolean){
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE) // Samakan nama file prefs
        with(sharedPref.edit()) {
            if (isLogin) {
                putBoolean("IS_LOGGED_IN", true)
                putLong("LOGIN_TIMESTAMP", System.currentTimeMillis())
            } else {
                // Saat logout, bersihkan semua data
                clear()
            }
            apply()
        }
    }

//    private fun observeLoading() {
//        viewModel.isLoading.observe(this) { isLoading ->
//            setLoadingState(isLoading)
//        }
//    }

    // Fungsi untuk mengubah tampilan tombol
    private fun setLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.loginButton.isEnabled = false
            binding.loginButton.text = "Loading..."
            // Tampilkan indikator putar bawaan MaterialButton
        } else {
            binding.loginButton.isEnabled = true
            binding.loginButton.text = "Login"
            // Sembunyikan indikator putar
        }
    }
}