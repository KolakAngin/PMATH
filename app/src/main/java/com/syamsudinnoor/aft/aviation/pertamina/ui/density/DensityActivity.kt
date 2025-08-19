package com.syamsudinnoor.aft.aviation.pertamina.ui.density

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityDensityBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter.SectionPagerAdapter
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = intent.getStringExtra(APP_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter

        val tabs : TabLayout = binding.tabs
        TabLayoutMediator(tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(tabsOfArray[position])
        }.attach()

    }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.help_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.help_menu -> Toast.makeText(this,"Ini adalah laman bantuan", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val APP_NAME = "APP_NAME"
        private val tabsOfArray = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}





//Duplicate class for safety
//class DensityActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDensityBinding
//    private val viewModel: DensityViewModel by viewModels {
//        val database = SnoorRoomDatabase.getDatabase(application)
//        val repository = SNoorRepository(database.sNoorDao())
//        ViewModelFactory(repository)
//    }
//
//    private var checkingDensityInput : Boolean = false
//    private var checkingTempInput : Boolean = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //enableEdgeToEdge()
//        binding = ActivityDensityBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        setSupportActionBar(binding.topAppBar)
//        supportActionBar?.title = intent.getStringExtra(APP_NAME)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//
//        setupDensity()
//        digitValidation()
//        binding.buttonSearch.setOnClickListener {
//            searchDensity()
//        }
//
//
//        viewModel.isFormValid.observe(this) { isValid ->
//            binding.buttonSearch.isEnabled  = isValid
//
//        }
//
//
//    }
//
//    private fun searchDensity() {
//        val density = binding.editTextDensity.text.toString().toDouble()
//        val temperature = binding.editTextTemp.text.toString().toDouble()
//        viewModel.searchDensity(density, temperature)
//    }
//
//    private fun setupDensity(){
//        viewModel.densityResult.observe(this) { result ->
//            if (result != null && binding.editTextIt.text.toString().isNotEmpty()) {
//                val formattedResult = "Hasil Ditemukan: ${result.result }"
//                binding.textViewResult.text = formattedResult
//                binding.imgResultIcon.visibility  = View.GONE
//            }else if (result != null){
//                val formattedResult = "Hasil Ditemukan: ${result}"
//                binding.textViewResult.text = formattedResult
//                binding.imgResultIcon.visibility  = View.GONE
//            }else {
//                binding.textViewResult.text = "Data tidak ditemukan."
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//                binding.imgResultIcon.visibility  = View.VISIBLE
//            }
//        }
//    }
//
//    private fun digitValidation() {
//        binding.editTextDensity.addTextChangedListener(object : TextWatcher{
//
//            override fun afterTextChanged(s: Editable?) {}
//
//            override fun beforeTextChanged(
//                s: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {}
//
//            override fun onTextChanged(
//                s: CharSequence?,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                densityDigitValidation(s.toString())
//            }
//
//        } )
//
//
//        binding.editTextTemp.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {}
//
//            override fun beforeTextChanged(
//                s: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {}
//
//            override fun onTextChanged(
//                s: CharSequence?,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                tempDigitValidation(s.toString())
//            }
//
//
//
//        })
//
//
//    }
//
//
//    private fun tempDigitValidation(digit: String) {
//        val lengthDigit = digit.trim().length
//        val regexPattern =  "^\\d{2}\\.\\d{1}$"
//        val regex = Regex(regexPattern)
//
//
//        if (digit.isNotEmpty()){
//            if(regex.matches(digit) || lengthDigit == 2){
//                binding.editTextTemp.error = null
//                viewModel.onDensityInputChanged(true)
//            }else{
//                binding.editTextTemp.error = "Format salah"
//                binding.buttonSearch.isEnabled = false
//                viewModel.onDensityInputChanged(false)
//            }
//        }
//
//    }
//
//    private fun densityDigitValidation(digit : String){
//        val regexPattern1 = "^\\d\\.\\d{3}$"
//        val regexPattern2 = "^\\d\\.\\d{4}$"
//        val regex1 = Regex(regexPattern1)
//        val regex2 = Regex(regexPattern2)
//
//        if(digit.isNotEmpty()){
//            if (regex1.matches(digit) || regex2.matches(digit)){
//                binding.editTextDensity.error = null
//                viewModel.onTemperatureInputChanged(true)
//            }else{
//                binding.editTextDensity.error = "Format salah"
//                binding.buttonSearch.isEnabled = false
//                viewModel.onTemperatureInputChanged(false)
//            }
//        }
//
//    }
//
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.help_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.help_menu -> Toast.makeText(this,"Ini adalah laman bantuan", Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    companion object{
//        const val APP_NAME = "APP_NAME"
//    }
//}