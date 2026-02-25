package com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.activity_holder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityVolumeControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter.SectionPagerAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter.SectionQualityControlAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder.BridgerQualityControlActivity
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder.BridgerQualityControlActivity.Companion.APP_NAME

class VolumeControlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVolumeControlBinding
    private var updateData : AnalisaVolumeControlWithDetail? = null
    private lateinit var viewPager : ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityVolumeControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))


        updateData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra("UPDATE_DATA", AnalisaVolumeControlWithDetail::class.java)
        }else{
            intent.getParcelableExtra("UPDATE_DATA")
        }

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = APP_NAME
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionQualityControlAdapter(updateData,this)
        viewPager = binding.viewPager

        viewPager.adapter = sectionPagerAdapter

        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quality_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.total_data ->{
                val intent = Intent(this, TotalVolumeControlActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    companion object{
        const val APP_NAME = "Volume Control"
        private val TAB = intArrayOf(
            R.string.vc_tab1,
            R.string.vc_tab2,
        )

    }
}