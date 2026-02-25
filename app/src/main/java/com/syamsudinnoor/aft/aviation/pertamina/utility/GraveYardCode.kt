package com.syamsudinnoor.aft.aviation.pertamina.utility

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityDippingTankBinding

//    private fun setupDensity(){
//        viewModel.densityResult.observe(viewLifecycleOwner) { result ->
//            if (result != null && densityFromDistributor != null) {
//                val formattedResult = "Hasil Ditemukan: ${result.result}, diffrensial : ${result.result!! - densityFromDistributor!!}"
//                resultDensity = result.result
//                diffMax = result.result - densityFromDistributor!!
//                Toast.makeText(requireContext(), "Hasil : ${result.result}, hasil diff : ${densityFromDistributor!!} pengurangan $diffMax", Toast.LENGTH_SHORT).show()
//                val difMaxText = when{
//                    ( result.result - densityFromDistributor!! >= TOLERENCE ) && (result.result - densityFromDistributor!! <= NEGATIVE_TOLERENCE)
//                        -> "OKE : {${result.result - densityFromDistributor!!}"
//                    else -> "NOT OKE : {${result.result - densityFromDistributor!!}"
//                }
//
//
//                binding.editDensityRsult.setText(result.result.toString())
//                binding.editDiffMax.setText(diffMax.toString())
//
//                binding.textViewResult.text = formattedResult + " " + difMaxText
//
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

//    private fun addHeaderContent(document: Document) {
//        document.add(
//            Paragraph("BRIDGER QUALITY CONTROL BEFORE RECEIPT RECORD")
//                .setBold().setFontSize(14f).setTextAlignment(TextAlignment.CENTER).setMarginBottom(15f)
//        )
//
//        val headerInfoTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 0.2f, 5f, 5f)))
//            .setWidth(UnitValue.createPercentValue(100f)).setMarginBottom(10f).setFixedLayout()
//
//        val reportDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
//        headerInfoTable.addCell(createBorderlessCell("AFT"))
//        headerInfoTable.addCell(createBorderlessCell(":"))
//        headerInfoTable.addCell(createBorderlessCell("SYAMSUDIN NOOR"))
//        headerInfoTable.addCell(createBorderlessCell("SF-103/2012 - AVS Rev.1").setTextAlignment(TextAlignment.RIGHT))
//        headerInfoTable.addCell(createBorderlessCell("DATE"))
//        headerInfoTable.addCell(createBorderlessCell(":"))
//        headerInfoTable.addCell(createBorderlessCell(reportDate))
//        headerInfoTable.addCell(createBorderlessCell(""))
//        headerInfoTable.addCell(createBorderlessCell("GRADE"))
//        headerInfoTable.addCell(createBorderlessCell(":"))
//        headerInfoTable.addCell(createBorderlessCell("JET A-1"))
//        headerInfoTable.addCell(createBorderlessCell(""))
//        document.add(headerInfoTable)
//    }

//    private fun addHeaderContent(document: Document, context: Context) {
//        // Tabel untuk menampung Logo dan Judul agar sejajar
//        val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(1.5f, 8.5f)))
//            .setWidth(UnitValue.createPercentValue(100f))
//
//        // Sel untuk Logo
//        try {
//            val drawable = ContextCompat.getDrawable(context, R.drawable.logo_pertamina) // GANTI NAMA FILE
//            val bitmap = (drawable as BitmapDrawable).bitmap
//            val stream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            val imageData = ImageDataFactory.create(stream.toByteArray())
//            val logo = Image(imageData).scaleToFit(80f, 80f) // Sesuaikan ukuran logo
//            headerTable.addCell(Cell().add(logo).setBorder(null))
//        } catch (e: Exception) {
//            // Jika logo tidak ditemukan, tambahkan sel kosong saja
//            headerTable.addCell(createBorderlessCell(" "))
//            e.printStackTrace()
//        }
//
//        // Sel untuk Judul dan Info
//        val titleAndInfoTable = Table(UnitValue.createPercentArray(1))
//        titleAndInfoTable.addCell(createBorderlessCell("BRIDGER QUALITY CONTROL BEFORE RECEIPT RECORD").setBold().setFontSize(14f).setTextAlignment(TextAlignment.CENTER))
//
//        val headerInfoTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 0.2f, 5f, 5f))).setWidth(UnitValue.createPercentValue(100f))
//        val reportDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
//        headerInfoTable.addCell(createBorderlessCell("AFT"))
//        headerInfoTable.addCell(createBorderlessCell(":"))
//        headerInfoTable.addCell(createBorderlessCell("SYAMSUDIN NOOR"))
//        headerInfoTable.addCell(createBorderlessCell("SF-103/2012 - AVS Rev.1").setTextAlignment(TextAlignment.RIGHT))
//        headerInfoTable.addCell(createBorderlessCell("DATE"))
//        headerInfoTable.addCell(createBorderlessCell(":"))
//        headerInfoTable.addCell(createBorderlessCell(reportDate))
//        headerInfoTable.addCell(createBorderlessCell(""))
//        headerInfoTable.addCell(createBorderlessCell("GRADE"))
//        headerInfoTable.addCell(createBorderlessCell(":"))
//        headerInfoTable.addCell(createBorderlessCell("JET A-1"))
//        headerInfoTable.addCell(createBorderlessCell(""))
//
//        titleAndInfoTable.addCell(Cell().add(headerInfoTable).setBorder(null))
//        headerTable.addCell(Cell().add(titleAndInfoTable).setBorder(null).setVerticalAlignment(VerticalAlignment.MIDDLE))
//
//        document.add(headerTable)
//
//    }


//    fun generatePdf(context: Context, data: List<BridgerQualityControl>) {
//        val pdfPath = context.getExternalFilesDir(null)?.absolutePath
//        val file = File(pdfPath, "BridgerQualityControlReport_Final.pdf")
//        val pdfWriter = PdfWriter(file)
//        val pdfDocument = PdfDocument(pdfWriter)
//
//        // Menggunakan A4 Landscape
//        val document = Document(pdfDocument, PageSize.A4.rotate())
//        document.setMargins(20f, 20f, 20f, 20f)
//
//        // Menambahkan Judul dan Info Header (AFT, DATE, GRADE)
//        addHeaderContent(document,context)
//
//        // Membuat Tabel Utama
//        val columnWidths = floatArrayOf(
//            1f, 1.5f, 2.5f, 2.5f, 2f, 2.5f, // Grup 1
//            3f, 2.5f,                      // Grup 2
//            3f, 2f, 2f, 2.5f,              // Grup 3
//            2f, 2f, 2.5f,                  // Grup 4
//            3f, 1.5f, 2f,                  // Grup 5
//            2.5f, 3.5f                     // Grup 6
//        )
//        val mainTable = Table(UnitValue.createPercentArray(columnWidths))
//            .setWidth(UnitValue.createPercentValue(100f))
//            .setTextAlignment(TextAlignment.CENTER)
//            .setFixedLayout()
//
//        // Menambahkan Header Tabel Sesuai Struktur Baru Anda
//        addTableHeader(mainTable)
//
//        // Mengisi Tabel dengan Data Sesuai Urutan Baru Anda
//        populateTableWithData(mainTable, data)
//
//        document.add(mainTable)
//        addFooterContent(document)
//        document.close()
//    }


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
//
//class DippingTankActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDippingTankBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //enableEdgeToEdge()
//        binding = ActivityDippingTankBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        setSupportActionBar(binding.topAppBar)
//        supportActionBar?.title = intent.getStringExtra(APP_NAME)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        setupDippingTank()
//        inputValidation()
//
//        viewModel.isAllItemValid.observe(this){isValid ->
//            binding.buttonSearch.isEnabled = isValid
//        }
//
//        binding.buttonSearch.setOnClickListener {
//            binding.editTextMm.clearFocus()
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(binding.editTextMm.windowToken, 0)
//            searchDippingTank()
//            binding.cardViewResult.setBackgroundResource(R.drawable.normal_card_background)
//        }
//
//    }
//
//
//    private fun searchDippingTank() {
//
//        val mm = binding.editTextMm.text.toString().toDouble()
//        when(statusTankSpinner){
//            "Tangki 9" -> viewModel.searchTangki9(mm)
//            "Tangki 10" -> viewModel.searchTangki10(mm)
//            "Tangki 11" -> viewModel.searchRangki11(mm)
//            else -> {}
//        }
//
//    }
//
//    fun setupDippingTank() {
//
//        viewModel.tangki9Result.observe(this) { result ->
//            if (result != null) {
//                literOnTank = result.liter
//                simplifyHolderViewModel(literOnTank,statusTankSpinner)
//
//            } else {
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//                binding.imgResultIcon.visibility = View.VISIBLE
//                binding.tableViewResult.visibility = View.GONE
//            }
//        }
//
//        viewModel.tangki10Result.observe(this) { result ->
//            if (result != null) {
//                literOnTank = result.liter ?: 0.0
//                simplifyHolderViewModel(literOnTank,statusTankSpinner)
//            } else {
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//                binding.tableViewResult.visibility = View.GONE
//                binding.imgResultIcon.visibility = View.VISIBLE
//            }
//        }
//
//        viewModel.tangki11Result.observe(this) { result ->
//            if (result != null) {
//                literOnTank = result.liter
//                simplifyHolderViewModel(literOnTank,statusTankSpinner)
//            } else {
//                binding.imgResultIcon.setImageResource(R.drawable.not_found)
//                binding.imgResultIcon.visibility = View.VISIBLE
//                binding.tableViewResult.visibility = View.GONE
//            }
//        }
//
//    }
//
//
//    private fun simplifyHolderViewModel(liter : Double?, tangkiName : String?){
//        binding.imgResultIcon.visibility = View.GONE
//        when(statusSection){
//            "Receiving Tank" -> {
//                statusReceiving(literOnTank ?: 0.0, statusTankSpinner ?: "")
//            }
//            "Distribution Tank" -> {
//                statusDistribution(literOnTank ?: 0.0, statusTankSpinner ?: "")
//            }
//            "Settle Tank" -> {
//                statusSettle(literOnTank ?: 0.0, statusTankSpinner ?: "")
//            }
//            else -> {
//                binding.tableViewResult.visibility = View.GONE
//            }
//        }
//    }
//
//
//    private fun inputValidation(){
//        binding.spinnerTank.addTextChangedListener(object : TextWatcher{
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
//                    statusTankSpinner = s.toString()
//                    viewModel.onTankSelected(true)
//                }else{
//                    viewModel.onTankSelected(false)
//                }
//
//            }
//
//        })
//        binding.rgSession.setOnCheckedChangeListener { _, checkedId ->
//            when(checkedId){
//                R.id.rb_receiving_tank -> {
//                    viewModel.onSessionSelected(true)
//                    this.statusSection = "Receiving Tank"
//                }
//                R.id.rb_distribution_tank -> {
//                    viewModel.onSessionSelected(true)
//                    this.statusSection = "Distribution Tank"
//                }
//                R.id.rb_settle_tank -> {
//                    viewModel.onSessionSelected(true)
//                    this.statusSection = "Settle Tank"
//                }else -> viewModel.onSessionSelected(false)
//            }
//        }
//
//        binding.editTextMm.addTextChangedListener(helperSettingEditText { text ->
//            if (text.isNotEmpty()){
//                viewModel.onMMInputValid(true)
//            }else{
//                viewModel.onMMInputValid(false)
//            }
//        })
//    }
//
//
//    private fun statusReceiving(liter: Double, tangkiName : String){
//        val maxCapsByTank = maxCapDecider(tangkiName)
//
//        val capacityRecomendation = maxCapsByTank - liter
//
//        binding.imgResultIcon.visibility = View.GONE
//        binding.tableViewResult.visibility = View.VISIBLE
//
//        binding.row1Header.text = getString(R.string.max_capacity_tank)
//        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(maxCapsByTank))
//
//        binding.row2Header.text = getString(R.string.dipping_result_tank)
//        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(liter))
//
//        binding.row3Header.text = getString(R.string.receiving_capacity_tank)
//        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(capacityRecomendation))
//
//        binding.row4Header.visibility = View.GONE
//        binding.row4Body.visibility = View.GONE
//
//        val percentage = (liter / maxCapsByTank) * 100
//        when{
//            percentage >= 75.0 ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
//            }
//            percentage >= 50.0 ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
//            }else ->{
//            binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
//        }
//        }
//
//    }
//
//    private fun maxCapDecider(tangkName : String) : Double{
//        return when(tangkName){
//            "Tangki 9" -> MAX_CAP_T9
//            "Tangki 10" -> MAX_CAP_T10
//            "Tangki 11" -> MAX_CAP_T11
//            else -> 0.0
//        }
//    }
//
//    private fun deathStockDecider(tangkName : String) : Double{
//        return  when(tangkName){
//            "Tangki 9" -> D_STOCK_T9
//            "Tangki 10" -> D_STOCK_T10
//            "Tangki 11" -> D_STOCK_T11
//            else -> 0.0
//        }
//    }
//
//    private fun statusDistribution(liter: Double, tangkiName : String){
//        val dStockByTank = deathStockDecider(tangkiName)
//
//        val maxDistribution = liter - dStockByTank
//        val isDistribution = when{
//            maxDistribution > 0.0 -> maxDistribution
//            else -> 0.0
//        }
//        val isPumpable = maxDistribution > 0.0
//
//        binding.imgResultIcon.visibility = View.GONE
//        binding.tableViewResult.visibility = View.VISIBLE
//        binding.row4Header.visibility = View.VISIBLE
//        binding.row4Body.visibility = View.VISIBLE
//
//        binding.row1Header.text = getString(R.string.death_stock_tank)
//        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(dStockByTank))
//
//        binding.row2Header.text = getString(R.string.dipping_result_tank)
//        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(liter))
//
//        binding.row3Header.text = getString(R.string.is_pumpable_tank)
//        binding.row3Body.text = isPumpable.toString()
//
//        binding.row4Header.text = getString(R.string.pumpable_stock_tank)
//        binding.row4Body.text = getString(R.string.liter_holder, numberFormatter(isDistribution))
//
//
//        when{
//            maxDistribution <= 0.0 ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
//            }
//
//            maxDistribution <= dStockByTank ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
//            }
//            else ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
//            }
//
//        }
//    }
//
//    private fun statusSettle(liter: Double, tangkiName : String){
//
//        val maxCaps = maxCapDecider(tangkiName)
//        val ullage = maxCaps - liter
//        val ullageLevel = ullage / maxCaps * 100
//
//
//        binding.imgResultIcon.visibility = View.GONE
//        binding.tableViewResult.visibility = View.VISIBLE
//
//
//        binding.row1Header.text = getString(R.string.max_capacity_tank)
//        binding.row1Body.text = getString(R.string.liter_holder, numberFormatter(maxCaps))
//
//        binding.row2Header.text = getString(R.string.dipping_result_tank)
//        binding.row2Body.text = getString(R.string.liter_holder, numberFormatter(liter))
//
//        binding.row3Header.text = getString(R.string.ullage_tank)
//        binding.row3Body.text = getString(R.string.liter_holder, numberFormatter(ullage))
//
//        binding.row4Header.visibility = View.GONE
//        binding.row4Body.visibility = View.GONE
//
//        when{
//            ullageLevel >= 75.0 ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.red_card_background)
//            }
//            ullageLevel >= 50.0 ->{
//                binding.cardViewResult.setBackgroundResource(R.drawable.yellow_card_background)
//            }else ->{
//            binding.cardViewResult.setBackgroundResource(R.drawable.green_card_background)
//        }
//        }
//
//    }
//
//
//
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
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val listOfTank = resources.getStringArray(R.array.tangks_choice)
//        val adapterTank = ArrayAdapter(this,R.layout.spinner_item_holder,listOfTank)
//        binding.spinnerTank.setAdapter(adapterTank)
//
//    }
//
//    companion object{
//        const val APP_NAME = "APP_NAME"
//
//        private const val MAX_CAP_T9 : Double = 98000.0
//        private const val MAX_CAP_T10 : Double = 499000.0
//        private const val MAX_CAP_T11 : Double = 1000000.0
//
//        private const val D_STOCK_T9 : Double = 5000.0
//        private const val D_STOCK_T10 : Double = 10000.0
//        private const val D_STOCK_T11 : Double = 30000.0
//
//
//    }
//}

//private val kompartemenStatus = mutableSetOf<String>()
//private lateinit var adapter1 : ArrayAdapter<String>
//private lateinit var adapter2 : ArrayAdapter<String>
//private lateinit var adapter3 : ArrayAdapter<String>
//
//adapter1 = ArrayAdapter(requireContext(), R.layout.spinner_item_holder,_kompartemenStatus.toMutableList())
//adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item_holder,_kompartemenStatus.toMutableList())
//adapter3 = ArrayAdapter(requireContext(), R.layout.spinner_item_holder,_kompartemenStatus.toMutableList())
//
//private fun updateDropDown(){
//    val filteredList = _kompartemenStatus.filter { it !in kompartemenStatus }
//
//    if(binding.spinnerKompartemen1.text.toString().isEmpty()){
//        adapter1.clear()
//        adapter1.addAll(filteredList)
//    }else if(binding.spinnerKompartemen2.text.toString().isEmpty()){
//        adapter2.clear()
//        adapter2.addAll(filteredList)
//    }else if(binding.spinnerKompartemen3.text.toString().isEmpty()) {
//        adapter3.clear()
//        adapter3.addAll(filteredList)
//
//    }
//
//}
//
//private fun setupListener(dropdown : AutoCompleteTextView, adapter : ArrayAdapter<String>){
//    dropdown.setAdapter(adapter)
//    dropdown.setOnItemClickListener { _, _, position, _ ->
//        Toast.makeText(requireContext(), "Item $kompartemenStatus clicked", Toast.LENGTH_SHORT).show()
//        val selectedItem = adapter.getItem(position)
//        if (selectedItem != null) {
//            kompartemenStatus.add(selectedItem)
//            updateDropDown()
//        }
//    }
//}
//
//binding.spinnerKompartemen1.setAdapter(adapter1)
//binding.spinnerKompartemen2.setAdapter(adapter2)
//binding.spinnerKompartemen3.setAdapter(adapter3)
//
//setupListener(binding.spinnerKompartemen1, adapter1)
//setupListener(binding.spinnerKompartemen2, adapter2)
//setupListener(binding.spinnerKompartemen3, adapter3)


//
//<androidx.cardview.widget.CardView
//android:layout_width="match_parent"
//android:layout_height="0dp"
//android:id="@+id/holder_data_analytics"
//app:layout_constraintTop_toTopOf="parent"
//android:layout_margin="10dp"
//app:cardCornerRadius="3dp"
//app:cardElevation="20dp"
//app:layout_constraintStart_toStartOf="parent">
//<androidx.constraintlayout.widget.ConstraintLayout
//android:layout_width="match_parent"
//android:layout_height="match_parent"
//android:layout_margin="10dp">
//
//<ImageView
//android:layout_width="100sp"
//android:layout_height="100sp"
//android:src="@drawable/ic_volume_control"
//android:id="@+id/img_bridger"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toTopOf="parent"/>
//
//<TextView
//app:layout_constraintTop_toTopOf="@id/img_bridger"
//app:layout_constraintStart_toEndOf="@+id/img_bridger"
//app:layout_constraintBottom_toBottomOf="@+id/img_bridger"
//android:id="@+id/txt_jumlah_bridger"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="1000"
//android:layout_marginStart="10dp"
//android:textSize="20sp"/>
//
//<ImageView
//android:layout_width="50sp"
//android:layout_height="50sp"
//android:src="@drawable/ic_volume_control"
//android:id="@+id/img_total_lost"
//android:visibility="gone"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/img_bridger"/>
//
//
//<TextView
//android:visibility="gone"
//app:layout_constraintTop_toTopOf="@+id/img_total_lost"
//app:layout_constraintStart_toEndOf="@+id/img_total_lost"
//app:layout_constraintBottom_toBottomOf="@+id/img_total_lost"
//android:id="@+id/txt_jumlah_lost"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="LOST"
//android:layout_marginStart="10dp"
//android:textSize="15sp"/>
//
//
//<TextView
//app:layout_constraintTop_toTopOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintBottom_toBottomOf="@+id/img_bridger"
//android:id="@+id/txt_plus_total"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="1000"
//android:layout_marginEnd="10dp"
//android:layout_marginStart="10dp"
//android:textSize="20sp"/>
//
//<ImageView
//android:layout_width="100sp"
//android:layout_height="100sp"
//android:layout_marginEnd="10dp"
//android:src="@drawable/up_arrow"
//android:id="@+id/img_plus_total"
//app:layout_constraintEnd_toStartOf="@+id/txt_plus_total"
//app:layout_constraintTop_toTopOf="@+id/img_bridger"/>
//
//
//<TextView
//android:visibility="gone"
//app:layout_constraintTop_toTopOf="@+id/img_total_lost"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintBottom_toBottomOf="@+id/img_total_lost"
//android:id="@+id/txt_minus_total"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="MINUS TOTAL"
//android:layout_marginEnd="10dp"
//android:layout_marginStart="10dp"
//android:textSize="15sp"/>
//
//
//<ImageView
//android:visibility="gone"
//android:layout_width="50sp"
//android:layout_height="50sp"
//android:layout_marginEnd="10dp"
//android:src="@drawable/ic_volume_control"
//android:id="@+id/img_minus_total"
//app:layout_constraintEnd_toStartOf="@+id/txt_minus_total"
//app:layout_constraintTop_toTopOf="@+id/img_total_lost"/>
//
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//</androidx.cardview.widget.CardView>