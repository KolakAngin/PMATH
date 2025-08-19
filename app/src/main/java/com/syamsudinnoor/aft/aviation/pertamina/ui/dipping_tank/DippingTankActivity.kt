package com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank

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
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityDippingTankBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter

class DippingTankActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDippingTankBinding
    private  var statusTank : String? = null
    private  var statusSection : String? = null

    private var literOnTank : Double? = null

    private val viewModel: DippingTankViewModel by viewModels {
        val database = SnoorRoomDatabase.getDatabase(application)
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDippingTankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = intent.getStringExtra(APP_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDippingTank()
        inputValidation()

        viewModel.isAllItemValid.observe(this){isValid ->
            binding.buttonSearch.isEnabled = isValid
        }

        binding.buttonSearch.setOnClickListener {
            searchDippingTank()
            binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
        }

    }


    private fun searchDippingTank() {

        val mm = binding.editTextMm.text.toString().toDouble()
        when(statusTank){
            "Tangki 9" -> viewModel.searchTangki9(mm)
            "Tangki 10" -> viewModel.searchTangki10(mm)
            "Tangki 11" -> viewModel.searchRangki11(mm)
            else -> {}
        }

    }

    fun setupDippingTank() {

        viewModel.tangki9Result.observe(this) { result ->
            if (result != null) {
                literOnTank = result.liter
                simplifyHolderViewModel(literOnTank,statusTank)

            } else {
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
            }
        }

        viewModel.tangki10Result.observe(this) { result ->
            if (result != null) {
                literOnTank = result.liter ?: 0.0
                simplifyHolderViewModel(literOnTank,statusTank)
            } else {
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.visibility = View.VISIBLE
            }
        }

        viewModel.tangki11Result.observe(this) { result ->
            if (result != null) {
                literOnTank = result.liter
                simplifyHolderViewModel(literOnTank,statusTank)
            } else {
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
            }
        }

    }


    private fun simplifyHolderViewModel(liter : Double?, tangkiName : String?){
        binding.imgResultIcon.visibility = View.GONE
        when(statusSection){
            "Receiving Tank" -> {
                statusReceiving(literOnTank ?: 0.0, statusTank ?: "")
            }
            "Distribution Tank" -> {
                statusDistribution(literOnTank ?: 0.0, statusTank ?: "")
            }
            "Settle Tank" -> {
                statusSettle(literOnTank ?: 0.0, statusTank ?: "")
            }
            else -> {
                binding.tableViewResult.visibility = View.GONE
            }
        }
    }


    private fun inputValidation(){
        binding.rgTangki.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_tangki9 -> {
                    viewModel.onTankSelected(true)
                    this.statusTank = "Tangki 9"
                }
                R.id.rb_tangki10 -> {
                    viewModel.onTankSelected(true)
                    this.statusTank = "Tangki 10"
                }
                R.id.rb_tangki11 -> {
                    viewModel.onTankSelected(true)
                    this.statusTank = "Tangki 11"

                }else -> viewModel.onTankSelected(false)
            }
        }

        binding.rgSession.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_receiving_tank -> {
                    viewModel.onSessionSelected(true)
                    this.statusSection = "Receiving Tank"
                }
                R.id.rb_distribution_tank -> {
                    viewModel.onSessionSelected(true)
                    this.statusSection = "Distribution Tank"
                }
                R.id.rb_settle_tank -> {
                    viewModel.onSessionSelected(true)
                    this.statusSection = "Settle Tank"
                }else -> viewModel.onSessionSelected(false)
            }
        }

        binding.editTextMm.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.toString().isNotEmpty()){
                 viewModel.onMMInputValid(true)
                }else{
                    viewModel.onMMInputValid(false)
                }
            }

        })
    }


    private fun statusReceiving(liter: Double, tangkiName : String){
        val maxCapsByTank = maxCapDecider(tangkiName)

        val capacityRecomendation = maxCapsByTank - liter

        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE

        binding.row1Header.text = getString(R.string.max_capacity_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(maxCapsByTank))

        binding.row2Header.text = getString(R.string.dipping_result_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(liter))

        binding.row3Header.text = getString(R.string.receiving_capacity_tank)
        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(capacityRecomendation))

        binding.row4Header.visibility = View.GONE
        binding.row4Body.visibility = View.GONE

        val percentage = (liter / maxCapsByTank) * 100
        when{
            percentage >= 75.0 ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
            }
            percentage >= 50.0 ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
            }else ->{
            binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
            }
        }

    }

    private fun maxCapDecider(tangkName : String) : Double{
        return when(tangkName){
            "Tangki 9" -> MAX_CAP_T9
            "Tangki 10" -> MAX_CAP_T10
            "Tangki 11" -> MAX_CAP_T11
            else -> 0.0
        }
    }

    private fun deathStockDecider(tangkName : String) : Double{
        return  when(tangkName){
            "Tangki 9" -> D_STOCK_T9
            "Tangki 10" -> D_STOCK_T10
            "Tangki 11" -> D_STOCK_T11
            else -> 0.0
        }
    }

    private fun statusDistribution(liter: Double, tangkiName : String){
        val dStockByTank = deathStockDecider(tangkiName)

        val maxDistribution = liter - dStockByTank
        val isDistribution = when{
            maxDistribution > 0.0 -> maxDistribution
            else -> 0.0
        }
        val isPumpable = maxDistribution > 0.0

        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE
        binding.row4Header.visibility = View.VISIBLE
        binding.row4Body.visibility = View.VISIBLE

        binding.row1Header.text = getString(R.string.death_stock_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(dStockByTank))

        binding.row2Header.text = getString(R.string.dipping_result_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(liter))

        binding.row3Header.text = getString(R.string.is_pumpable_tank)
        binding.row3Body.text = isPumpable.toString()

        binding.row4Header.text = getString(R.string.pumpable_stock_tank)
        binding.row4Body.text = getString(R.string.liter_holder, numberFormatter(isDistribution))


        when{
            maxDistribution <= 0.0 ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
            }

            maxDistribution <= dStockByTank ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
            }
            else ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
            }

        }
    }

    private fun statusSettle(liter: Double, tangkiName : String){

        val maxCaps = maxCapDecider(tangkiName)
        val ullage = maxCaps - liter
        val ullageLevel = ullage / maxCaps * 100


        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE


        binding.row1Header.text = getString(R.string.max_capacity_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(maxCaps))

        binding.row2Header.text = getString(R.string.dipping_result_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(liter))

        binding.row3Header.text = getString(R.string.ullage_tank)
        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(ullage))

        binding.row4Header.visibility = View.GONE
        binding.row4Body.visibility = View.GONE

        when{
            ullageLevel >= 75.0 ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
            }
            ullageLevel >= 50.0 ->{
                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
            }else ->{
            binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
            }
        }

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

        private const val MAX_CAP_T9 : Double = 98000.0
        private const val MAX_CAP_T10 : Double = 499000.0
        private const val MAX_CAP_T11 : Double = 1000000.0

        private const val D_STOCK_T9 : Double = 5000.0
        private const val D_STOCK_T10 : Double = 10000.0
        private const val D_STOCK_T11 : Double = 30000.0


    }
}