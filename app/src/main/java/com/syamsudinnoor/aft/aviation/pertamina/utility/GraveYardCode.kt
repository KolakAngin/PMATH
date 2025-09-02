package com.syamsudinnoor.aft.aviation.pertamina.utility

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