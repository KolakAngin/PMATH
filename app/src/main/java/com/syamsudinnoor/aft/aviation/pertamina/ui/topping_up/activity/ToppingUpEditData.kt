package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityToppingUpBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityToppingUpEditDataBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.KonsinyasiFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.ToppingUpInputFragment

class ToppingUpEditData : AppCompatActivity() {
    private var updateData : ToppingUp? = null
    private lateinit var  binding : ActivityToppingUpEditDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToppingUpEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundResource(R.color.colorPrimary)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Edit Data Topping Up"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra("UPDATE_DATA", ToppingUp::class.java)

        }else{
            intent.getParcelableExtra("UPDATE_DATA")
        }

        if (updateData?.status == KonsinyasiFragment.STATUS){
            supportActionBar?.title = "Edit Data Konsinyasi"
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentHolder.id, KonsinyasiFragment.newInstance(updateData))
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentHolder.id, ToppingUpInputFragment.newInstance(updateData))
                .commit()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home ->{
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}