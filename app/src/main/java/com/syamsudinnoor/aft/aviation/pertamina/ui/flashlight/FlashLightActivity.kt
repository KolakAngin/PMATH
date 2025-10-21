package com.syamsudinnoor.aft.aviation.pertamina.ui.flashlight

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityFlashLightBinding
import java.util.jar.Manifest

class FlashLightActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFlashLightBinding

    private lateinit var cameraManager : CameraManager
    private var cameraId : String? = null
    private var isFlashOn : Boolean = false
    private var isSOSActive = false
    private var sosThread: Thread? = null

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if (!isGranted){
            Toast.makeText(this,"Izin diperlukan untuk mengakses flashlight!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashLightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundResource(R.color.colorPrimary)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Flashlight"
        val bool = intent.getBooleanExtra("SOS", false)
        if (bool){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }else{
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        binding.buttonFlashLight.setOnClickListener {
            if (hashCameraGranted()){
                toogleFlashOn()
            }else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        binding.buttonSos.setOnClickListener {
            if (hashCameraGranted()){
                toggleSOS()
            }else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun toogleFlashOn() {
        try{
            cameraId?.let {
                isFlashOn = !isFlashOn
                binding.imgFlashLight.setImageResource(if (isFlashOn) R.drawable.flash_light_on else R.drawable.flash_light_of)
                cameraManager.setTorchMode(it,isFlashOn)

            }
        }catch (e : CameraAccessException){
            Toast.makeText(this,"Gagal mengakses kamera!", Toast.LENGTH_LONG).show()
        }
    }

    private fun hashCameraGranted() : Boolean{
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun toggleSOS() {
        if (isSOSActive) {
            // stop SOS
            isSOSActive = false
            binding.imgSos.setImageResource(R.drawable.sos_off)
            sosThread?.interrupt() // hentikan thread
            cameraId?.let { cameraManager.setTorchMode(it, false) }
        } else {
            // start SOS
            isSOSActive = true
            binding.imgSos.setImageResource(R.drawable.sos_on)
            startSOS()
        }
    }

    private fun startSOS() {
        val dotDuration = 200L  // ms
        val dashDuration = dotDuration * 3
        val gap = dotDuration

        val sosPattern = listOf(
            dotDuration, gap, dotDuration, gap, dotDuration, // S = ...
            dashDuration, gap, dashDuration, gap, dashDuration, // O = ---
            dotDuration, gap, dotDuration, gap, dotDuration // S = ...
        )

        sosThread = Thread {
            try {
                while (isSOSActive) { // looping terus selama aktif
                    sosPattern.forEachIndexed { index, duration ->
                        if (!isSOSActive) return@Thread // break kalau user stop
                        if (index % 2 == 0) {
                            cameraId?.let { cameraManager.setTorchMode(it, true) }
                        } else {
                            cameraId?.let { cameraManager.setTorchMode(it, false) }
                        }
                        Thread.sleep(duration)
                    }
                    // jeda antar SOS
                    Thread.sleep(dotDuration * 3)
                }
            } catch (e: InterruptedException) {
                // thread di-interrupt
                cameraId?.let { cameraManager.setTorchMode(it, false) }
            }
        }
        sosThread?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        try{
            cameraManager.setTorchMode(cameraId!!,false)
            sosThread?.interrupt()
        }catch (e : NullPointerException){

        }


    }
}