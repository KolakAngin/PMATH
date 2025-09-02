package com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityBridgerQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter.SectionPagerAdapter

class BridgerQualityControlActivity : AppCompatActivity() {


    private lateinit var  binding : ActivityBridgerQualityControlBinding

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

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager = binding.viewPager

        viewPager.adapter = sectionPagerAdapter

        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB[position])
        }.attach()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.help_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.help_menu -> Toast.makeText(this,"Ini adalah laman bantuan", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val APP_NAME = "Bridger Quality Control"
        private val TAB = intArrayOf(
            R.string.tab_1,
            R.string.tab_3,
        )
    }
}