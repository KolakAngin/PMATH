package com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.FragmentDippingRefeullerBinding
import com.syamsudinnoor.aft.aviation.pertamina.factoryviewmodel.ViewModelFactory
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.SnoorRoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository.SNoorRepository
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.viewmodel.ToppingUpViewModel
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter


class FragmentDippingRefeuller : Fragment() {

    private  var _binding : FragmentDippingRefeullerBinding? = null


    private var statusRefeuller : String? = null
    private var statusSession : String? = null

    private var statusLiter : Double = 0.0
    private val viewModel: ToppingUpViewModel by viewModels {
        val database = SnoorRoomDatabase.getDatabase(requireContext())
        val repository = SNoorRepository(database.sNoorDao())
        ViewModelFactory(repository)
    }

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDippingRefeullerBinding.inflate(inflater, container, false)

        viewModel.isValid.observe(viewLifecycleOwner){
            binding.buttonSearch.isEnabled = it
        }

        validateInput()
        setupToppingUp()
        searchToppingUp()

        return binding.root
    }

    private fun searchToppingUp() {
        binding.buttonSearch.setOnClickListener {

            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.editTextMm.windowToken, 0)
            binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
            val value = binding.editTextMm.text.toString().toDouble()

            when(statusRefeuller){
                "SNR 17" -> {
                    statusLiter = binding.editTextMm.text.toString().toDouble()

                    simplifySetupToppingUp(this.statusSession)
                }
                "SNR 19" -> {
                    statusLiter = binding.editTextMm.text.toString().toDouble()
                    simplifySetupToppingUp(this.statusSession)
                }
                "SNR 20" -> viewModel.getSnr20(value)
                "SNR 21" -> viewModel.getSnr21(value)
                "SNR 22" -> viewModel.getSnr22(value)
                else -> {}
            }
        }
    }

    private fun setupToppingUp() {

        viewModel.snr20.observe(viewLifecycleOwner){ liter ->
            if(liter != null){
                this.statusLiter = liter.liter ?: 0.0
                simplifySetupToppingUp(this.statusSession)
            }else{
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
            }

        }

        viewModel.snr21.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                this.statusLiter = liter.liter ?: 0.0
                simplifySetupToppingUp(this.statusSession)
            }else{
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

        viewModel.snr22.observe(viewLifecycleOwner){liter ->
            if (liter != null){
                Log.d("TAG", "setupToppingUp: $liter")
                this.statusLiter = liter.liter ?: 0.0
                simplifySetupToppingUp(this.statusSession)
            }else{
                binding.imgResultIcon.visibility = View.VISIBLE
                binding.tableViewResult.visibility = View.GONE
                binding.imgResultIcon.setImageResource(R.drawable.not_found)
            }
        }

    }


    private fun simplifySetupToppingUp(status: String?){
        when(status){
            "Topping Up" -> statusToppingUp()
            "Settle" -> statusSettle()
        }
    }

    private fun statusToppingUp(){
        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE

        val maxCaps = when{
            statusRefeuller == "SNR 17" || statusRefeuller == "SNR 21" || statusRefeuller == "SNR 22" -> CONST_17_21_22
            else -> CONST_19_20
        }


        val decision = (statusLiter / maxCaps) * 100
        val textDecision = when{
            decision >= 50 ->"Belum butuh Topping Up"
            decision >= 25 -> "Akan butuh Topping Up"
            else -> "Secepatnya Topping Up"
        }

        binding.row1Header.text = resources.getString(R.string.dipping_result_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(statusLiter))
        binding.row2Header.text = getString(R.string.max_capacity_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(maxCaps))
        binding.row3Header.text = resources.getString(R.string.decision_topping_up)
        binding.row3Body.text = textDecision
        binding.row4Header.visibility = View.GONE
        binding.row4Body.visibility = View.GONE


        when{
            decision >= 50 -> binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)

            decision >= 25 -> binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)

            else -> binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
        }

    }

    private fun statusSettle(){
        binding.imgResultIcon.visibility = View.GONE
        binding.tableViewResult.visibility = View.VISIBLE

        val maxCaps = when{
            statusRefeuller == "SNR 17" || statusRefeuller == "SNR 21" || statusRefeuller == "SNR 22" -> CONST_17_21_22
            else -> CONST_19_20
        }

        val ullage = maxCaps - statusLiter
        val ullagePercent = (ullage / maxCaps) * 100


        binding.row1Header.text = resources.getString(R.string.dipping_result_tank)
        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(statusLiter))

        binding.row2Header.text = resources.getString(R.string.max_capacity_tank)
        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(maxCaps))

        binding.row3Header.text = resources.getString(R.string.ullage_tank)
        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(ullage))

        binding.row4Header.visibility = View.GONE
        binding.row4Body.visibility = View.GONE


        when{
            ullagePercent >= 75 -> binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)

            ullagePercent >= 50 -> binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)

            else -> binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
        }

    }


    fun validateInput() {

        binding.spinnerRefeuller.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

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
                val watching = s.toString()
                if (watching.isNotEmpty()) {
                    statusRefeuller = watching
                    viewModel.onRefeullerSelected(true)
                    if (watching == "SNR 17" || watching == "SNR 19") {
                        binding.containterMM.hint = getString(R.string.inset_liter)
                    } else {
                        binding.containterMM.hint = getString(R.string.insert_mm)
                    }
                } else {
                    viewModel.onRefeullerSelected(false)
                }
            }
        })

        binding.rgSession.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_topping_up ->{
                    this.statusSession = "Topping Up"
                    viewModel.onSessionSelected(true)
                }
                R.id.rb_settle_refeuller ->{
                    this.statusSession = "Settle"
                    viewModel.onSessionSelected(true)
                }
                else -> {
                    viewModel.onSessionSelected(false)
                }
            }
        }

        binding.editTextMm.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.toString().isNotEmpty()){
                    viewModel.onMMValid(true)
                }else{
                    viewModel.onMMValid(false)
                }
            }

        })

    }

    override fun onResume() {
        super.onResume()
        val refuellers = resources.getStringArray(R.array.refeullers_choice)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_holder, refuellers)
        binding.spinnerRefeuller.setAdapter(arrayAdapter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val APP_NAME = "APP_NAME"
        private const val CONST_17_21_22 = 24500.0
        private const val CONST_19_20 = 25000.0

    }
}