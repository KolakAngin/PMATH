package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityKonsinyasiBinding
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.KonsinyasiFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.ToppingUpInputFragment

class KonsinyasiActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityKonsinyasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonsinyasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(getColor(R.color.colorPrimary))

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = TITLE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .add(binding.holderFragment.id, KonsinyasiFragment())
            .commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when{
            item.itemId == android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    companion object{
        const val TITLE = "Konsinyasi"
    }
}