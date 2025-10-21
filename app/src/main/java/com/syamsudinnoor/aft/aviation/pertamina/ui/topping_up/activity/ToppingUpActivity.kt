package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityToppingUpBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.adapter.ToppingUpSectionPagerAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel

class ToppingUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToppingUpBinding

    private  var updateData : ToppingUp? = null
    private var statusIsUpdate : Boolean = false

    private var statusRefeuller : String? = null
    private var statusSession : String? = null

    private var statusLiter : Double = 0.0

    private lateinit var viewPager : ViewPager2

    private val viewModel: ToppingUpViewModel by viewModels {
        val database = SnoorRoomDatabase.Companion.getDatabase(application)
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToppingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        //basic setting actionbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = APP_NAME
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateData = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra("UPDATE_DATA", ToppingUp::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("UPDATE_DATA")
        }
        val currentIndex = intent.getIntExtra("DETAIL_INDEX", 0)
        statusIsUpdate = updateData != null


        val adapter = ToppingUpSectionPagerAdapter(updateData,this)
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        viewPager.currentItem = currentIndex
        val tabs = binding.tabs
        viewPager.currentItem = currentIndex
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = if(statusIsUpdate) "Update Data Topping Up" else "Input Data Topping Up"
                1 -> tab.text = "Data Topping Up"
            }
        }.attach()



    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.help_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

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



    companion object {
        const val APP_NAME = "Topping Up Refueller"
        private const val CONSTANTA = 25000.0
    }
}