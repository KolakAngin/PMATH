package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityBridgerDataShowActivtyBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl

class BridgerDataShowActivty : AppCompatActivity() {

    private var _binding : ActivityBridgerDataShowActivtyBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityBridgerDataShowActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val reportData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(APP_NAME, BridgerQualityControl::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(APP_NAME)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val APP_NAME = "Quality Control"
    }
}