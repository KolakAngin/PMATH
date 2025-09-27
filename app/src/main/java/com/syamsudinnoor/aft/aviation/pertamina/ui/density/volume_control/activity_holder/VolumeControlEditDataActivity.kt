package com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.activity_holder

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityVolumeControlEditDataBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.fragment.VolumeControlFragment

class VolumeControlEditDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVolumeControlEditDataBinding
    private var updateData : AnalisaVolumeControlWithDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityVolumeControlEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Edit Data"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra("UPDATE_DATA", AnalisaVolumeControlWithDetail::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("UPDATE_DATA")

        }

        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentHolder.id, VolumeControlFragment.newInstance(updateData))
            .commit()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                finish()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }
}