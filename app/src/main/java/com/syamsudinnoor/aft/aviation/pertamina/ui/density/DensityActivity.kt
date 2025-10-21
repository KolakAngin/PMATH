package com.syamsudinnoor.aft.aviation.pertamina.ui.density

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.adapter.ItemMenu
import com.syamsudinnoor.aft.aviation.pertamina.adapter.MainAdapter
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityDensityBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter.SectionPagerAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.activity_holder.BridgerQualityControlActivity
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.viewmodel.DensityViewModel
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.activity_holder.VolumeControlActivity
import kotlin.getValue

class DensityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDensityBinding
    private val viewModel: DensityViewModel by viewModels {
        val database = SnoorRoomDatabase.getDatabase(application)
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }

    private var checkingDensityInput : Boolean = false
    private var checkingTempInput : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDensityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Penerimaan Bridger"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvInitialize()

    }

    fun rvInitialize(){
        val menuAdapter = MainAdapter(getItemMenu())
        binding.rvHolder.adapter = menuAdapter
        binding.rvHolder.layoutManager = GridLayoutManager(this,2)
        binding.rvHolder.setHasFixedSize(true)

        menuAdapter.adapterPassItemCallback { itemMenu ->
            when(itemMenu.name){
                "Bridger Quality Control" -> {
                    val intent = Intent(this, BridgerQualityControlActivity::class.java)
                    startActivity(intent)
                }

                "Volume Control" -> {
                    val intent = Intent(this, VolumeControlActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.help_menu,menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.help_menu -> Toast.makeText(this,"Ini adalah laman bantuan", Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun getItemMenu(): ArrayList<ItemMenu> {
        val menuName = resources.getStringArray(R.array.holder_name_density)
        val menuImage = resources.obtainTypedArray(R.array.holder_image_density)
        val menuList = ArrayList<ItemMenu>()
        for (i in menuName.indices){
            val itemMenu = ItemMenu(menuName[i], menuImage.getResourceId(i, -1))
            menuList.add(itemMenu)
        }
        return menuList
    }

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
        const val APP_NAME = "APP_NAME"
        private val tabsOfArray = intArrayOf(
            R.string.tab_1,
            R.string.tab_3,

        )
    }
}




