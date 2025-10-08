package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityBridgerQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter.SectionPagerAdapter

class BridgerQualityControlActivity : AppCompatActivity() {


    private lateinit var  binding : ActivityBridgerQualityControlBinding
    private lateinit var viewPager : ViewPager2

    private var updateData : BridgerQualityControl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityBridgerQualityControlBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = APP_NAME
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateData = when{
            Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra("UPDATE_DATA",
                BridgerQualityControl::class.java)
            else -> @Suppress("DEPRECATION") intent.getParcelableExtra("UPDATE_DATA")
        }
        val sectionPagerAdapter = SectionPagerAdapter(updateData,this)
        viewPager = binding.viewPager

        viewPager.adapter = sectionPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Quality Control"
                1 -> tab.text = "Data Quality Control"
            }
        }.attach()


    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.help_menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object{
        const val APP_NAME = "Bridger Quality Control"
        private val TAB = intArrayOf(
            R.string.tab_1,
            R.string.tab_3,
        )
    }
}