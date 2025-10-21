package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityBridgerQualityDataEditBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.fragment.QualityControlFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.fragment.VolumeControlFragment

class BridgerQualityDataEdit : AppCompatActivity() {
    private lateinit var binding : ActivityBridgerQualityDataEditBinding
    private  var updateData : BridgerQualityControl? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityBridgerQualityDataEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Edit Data"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra("UPDATE_DATA", BridgerQualityControl::class.java)
        }else{
            intent.getParcelableExtra("UPDATE_DATA")
        }

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentHolder.id,
                QualityControlFragment.newInstance(updateData))
            .commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                finish()
                true
            }else ->
                super.onOptionsItemSelected(item)
        }
    }
}