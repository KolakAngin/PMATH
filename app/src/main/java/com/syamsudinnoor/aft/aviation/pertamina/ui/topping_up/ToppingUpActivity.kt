package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up


//
//
//class ToppingUpActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityToppingUpBinding
//
//    private var statusRefeuller : String? = null
//    private var statusSession : String? = null
//
//    private var statusLiter : Double = 0.0
//    private val viewModel: ToppingUpViewModel by viewModels {
//        val database = SnoorRoomDatabase.getDatabase(application)
//        val repository = SNoorRepository(database.sNoorDao())
//        ViewModelFactory(repository)
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //enableEdgeToEdge()
//        binding = ActivityToppingUpBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        //basic setting actionbar
//        setSupportActionBar(binding.topAppBar)
//        supportActionBar?.title = intent.getStringExtra(APP_NAME)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        //setup listener
//        validateInput()
//        setupToppingUp()
//        searchToppingUp()
//
//        viewModel.isValid.observe(this){ it ->
//            binding.buttonSearch.isEnabled  = it
//
//        }
//
//    }
//
//    private fun searchToppingUp() {
//        binding.buttonSearch.setOnClickListener {
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(binding.editTextMm.windowToken, 0)
//            binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
//            val value = binding.editTextMm.text.toString().toDouble()
//            Log.d("TAG", "searchToppingUp: $value Status $statusRefeuller : $statusRefeuller")
//            when(statusRefeuller){
//                "SNR 17" -> {
//                    statusLiter = binding.editTextMm.text.toString().toDouble()
//                    simplifySetupToppingUp(this.statusSession)
//                }
//                "SNR 19" -> {
//                    statusLiter = binding.editTextMm.text.toString().toDouble()
//                    simplifySetupToppingUp(this.statusSession)
//                }
//                "SNR 20" -> viewModel.getSnr20(value)
//                "SNR 21" -> viewModel.getSnr21(value)
//                "SNR 22" -> viewModel.getSnr22(value)
//                else -> {}
//            }
//        }
//    }
//
//    private fun setupToppingUp() {
//
//        viewModel.snr20.observe(this){ liter ->
//            if(liter != null){
//                this.statusLiter = liter.liter ?: 0.0
//                simplifySetupToppingUp(this.statusSession)
//            }else{
//                binding.imgResultIcon.visibility = View.VISIBLE
//                binding.tableViewResult.visibility = View.GONE
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//            }
//
//        }
//
//        viewModel.snr21.observe(this){liter ->
//            if (liter != null){
//                this.statusLiter = liter.liter ?: 0.0
//                simplifySetupToppingUp(this.statusSession)
//            }else{
//                binding.imgResultIcon.visibility = View.VISIBLE
//                binding.tableViewResult.visibility = View.GONE
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//            }
//        }
//
//        viewModel.snr22.observe(this){liter ->
//            if (liter != null){
//                this.statusLiter = liter.liter ?: 0.0
//                simplifySetupToppingUp(this.statusSession)
//            }else{
//                binding.imgResultIcon.visibility = View.VISIBLE
//                binding.tableViewResult.visibility = View.GONE
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//            }
//        }
//
//    }
//
//
//
//    private fun simplifySetupToppingUp(status: String?){
//        when(status){
//            "Topping Up" -> statusToppingUp()
//            "Settle" -> statusSettle()
//        }
//    }
//
//    private fun statusToppingUp(){
//        binding.imgResultIcon.visibility = View.GONE
//        binding.tableViewResult.visibility = View.VISIBLE
//
//
//        val decision = (statusLiter / CONSTANTA) * 100
//        val textDecision = when{
//            decision >= 50 ->"Belum butuh Topping Up"
//            decision >= 25 -> "Akan butuh Topping Up"
//            else -> "Secepatnya Topping Up"
//        }
//
//        binding.row1Header.text = resources.getString(R.string.dipping_result_tank)
//        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(statusLiter))
//        binding.row2Header.text = getString(R.string.max_capacity_tank)
//        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(CONSTANTA))
//        binding.row3Header.text = resources.getString(R.string.decision_topping_up)
//        binding.row3Body.text = textDecision
//        binding.row4Header.visibility = View.GONE
//        binding.row4Body.visibility = View.GONE
//
//
//        when{
//            decision >= 50 -> binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
//
//            decision >= 25 -> binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
//
//            else -> binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
//        }
//
//    }
//
//    private fun statusSettle(){
//        binding.imgResultIcon.visibility = View.GONE
//        binding.tableViewResult.visibility = View.VISIBLE
//
//
//        val ullage = CONSTANTA - statusLiter
//        val ullagePercent = (ullage / CONSTANTA) * 100
//
//
//        binding.row1Header.text = resources.getString(R.string.dipping_result_tank)
//        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(statusLiter))
//
//        binding.row2Header.text = resources.getString(R.string.max_capacity_tank)
//        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(CONSTANTA))
//
//        binding.row3Header.text = resources.getString(R.string.ullage_tank)
//        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(ullage))
//
//        binding.row4Header.visibility = View.GONE
//        binding.row4Body.visibility = View.GONE
//
//
//        when{
//            ullagePercent >= 75 -> binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
//
//            ullagePercent >= 50 -> binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
//
//            else -> binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
//        }
//
//    }
//
//
//
//    fun validateInput(){
//
//        binding.spinnerRefeuller.addTextChangedListener(object : TextWatcher{
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
//                val watching = s.toString()
//                if (watching.isNotEmpty()){
//                    statusRefeuller = watching
//                    viewModel.onRefeullerSelected(true)
//                    if(watching == "SNR 17" || watching == "SNR 19"){
//                        binding.containterMM.hint = getString(R.string.inset_liter)
//                    }
//                    else{
//                        binding.containterMM.hint = getString(R.string.insert_mm)
//                    }
//                }else{
//                    viewModel.onRefeullerSelected(false)
//                }
//            }
//        })
//
//
//        binding.rgSession.setOnCheckedChangeListener { _, checkedId ->
//            when(checkedId){
//                R.id.rb_topping_up ->{
//                    this.statusSession = "Topping Up"
//                    viewModel.onSessionSelected(true)
//                }
//                R.id.rb_settle_refeuller ->{
//                    this.statusSession = "Settle"
//                    viewModel.onSessionSelected(true)
//                }
//                else -> {
//                    viewModel.onSessionSelected(false)
//                }
//            }
//
//
//        }
//
//
//        binding.editTextMm.addTextChangedListener(object : TextWatcher{
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
//                if (s.toString().isNotEmpty()){
//                    viewModel.onMMValid(true)
//                }else{
//                    viewModel.onMMValid(false)
//                }
//            }
//
//        })
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val refeullers = resources.getStringArray(R.array.refeullers_choice)
//        val adapter = ArrayAdapter(this, R.layout.spinner_item_holder, refeullers)
//        binding.spinnerRefeuller.setAdapter(adapter)
//    }
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
//
//    companion object {
//        const val APP_NAME = "APP_NAME"
//        private const val CONSTANTA = 25000.0
//    }
//}