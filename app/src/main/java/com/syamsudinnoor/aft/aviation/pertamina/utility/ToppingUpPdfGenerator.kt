import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// Pastikan data class Anda ada di dalam proyek
// data class ToppingUp(...)

class ToppingUpPdfGenerator {

    // Fungsi utama untuk generate PDF, menyimpan ke folder Download, dan mengembalikan Uri
    fun generatePdf(context: Context, data: List<ToppingUp>): Uri? {
        val fileName = "ToppingUpReport_${System.currentTimeMillis()}.pdf"
        var pdfUri: Uri? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            pdfUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            pdfUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    val writer = PdfWriter(outputStream)
                    val pdfDocument = PdfDocument(writer)
                    // Menggunakan orientasi Portrait (tegak) untuk form ini
                    val document = Document(pdfDocument, PageSize.A4)
                    buildPdfContent(document, context, data)
                }
            }
        } else { // Android 9 ke bawah
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            val file = File(downloadsDir, fileName)
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument, PageSize.A4)
            buildPdfContent(document, context, data)
            pdfUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }
        return pdfUri
    }

    // Fungsi untuk membangun seluruh konten PDF
    private fun buildPdfContent(document: Document, context: Context, data: List<ToppingUp>) {
        document.setMargins(30f, 30f, 30f, 30f)
        addHeaderContent(document, context)
        val mainTable = createDataTable(data)
        document.add(mainTable)
        addFooterContent(document)
        document.close()
    }

    // ... import statements

    // ================== FUNGSI HEADER FINAL (LENGKAP & SEIMBANG) ==================
    private fun addHeaderContent(document: Document, context: Context) {
        // ---- BAGIAN 1: LOGO, JUDUL UTAMA, DAN ISO (YANG HILANG SEBELUMNYA) ----
        val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(1.5f, 7f, 1.5f)))
            .setWidth(UnitValue.createPercentValue(100f))

        // Kolom 1: Logo
        val logoCell = createBorderlessCell("")
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
        try {
            val drawable = ContextCompat.getDrawable(context, R.drawable.logo_pertamina) // GANTI NAMA FILE ANDA
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageData = ImageDataFactory.create(stream.toByteArray())
            val logo = Image(imageData).scaleToFit(100f, 100f)
            logoCell.add(logo)
        } catch (e: Exception) {
            logoCell.add(Paragraph(" "))
        }
        headerTable.addCell(logoCell)

        // Kolom 2: Judul
        val titleCell = createBorderlessCell("")
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
        titleCell.add(Paragraph("PERMINTAAN TOPING UP REFUELLER ").setBold().setFontSize(12f).setMargin(0f).setPadding(0f))
        titleCell.add(Paragraph("(DARI  FUNGSI PSI KEPADA FUNGSI  PP)").setBold().setFontSize(12f).setMargin(0f).setPadding(0f))
        headerTable.addCell(titleCell)

        // Kolom 3: Info Implementasi
        val infoCell = createBorderlessCell("")
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
        infoCell.add(Paragraph("IMPLEMENTASI").setBold().setFontSize(9f).setMargin(0f).setPadding(0f))
        infoCell.add(Paragraph("ISO 9001/2000").setFontSize(9f).setMargin(0f).setPadding(0f))
        headerTable.addCell(infoCell)
        // JANGAN LUPA TAMBAHKAN headerTable KE DOKUMEN
        document.add(headerTable)


        // ---- BAGIAN 2: INFO AFT, TANGGAL, REVISI (YANG SUDAH BENAR) ----
        val subHeaderTable = Table(UnitValue.createPercentArray(floatArrayOf(2f, 5f, 3f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(5f)

        // Mengambil tanggal dan hari ini
        val today = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        val dayFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
        val tanggalHariIni = dateFormat.format(today)
        val hariIni = dayFormat.format(today)

        // Kolom Kiri
        val leftInfoTable = Table(UnitValue.createPercentArray(floatArrayOf(0.2f, 0.2f, 3.8f)))
        leftInfoTable.addCell(createBorderlessCell("AFT").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell(":").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell("SYAMSUDIN NOOR").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell("TANGGAL").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell(":").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell(tanggalHariIni).setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell("HARI").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell(":").setFontSize(9f))
        leftInfoTable.addCell(createBorderlessCell(hariIni).setFontSize(9f))
        subHeaderTable.addCell(createBorderlessCell("").add(leftInfoTable))

        // Kolom Tengah (kosong)
        subHeaderTable.addCell(createBorderlessCell(""))

        // Kolom Kanan
        val rightInfoTable = Table(UnitValue.createPercentArray(floatArrayOf(2f, 0.2f, 3.8f)))
        rightInfoTable.addCell(createBorderlessCell("").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("IV-216 / E 26G16").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("REVISI : 0").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("").setFontSize(9f))
        rightInfoTable.addCell(createBorderlessCell("TANGGAL : 09.08.2011").setFontSize(9f))
        subHeaderTable.addCell(createBorderlessCell("").add(rightInfoTable))

        document.add(subHeaderTable)
    }


    private fun createDataTable(data: List<ToppingUp>): Table {
        val columnWidths = floatArrayOf(1f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 2f, 2f, 1.5f, 2.5f, 2f, 1.5f)
        val table = Table(UnitValue.createPercentArray(columnWidths))
            .setWidth(UnitValue.createPercentValue(100f))
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(15f)

        addTableHeader(table)
        populateTableWithData(table, data)
        return table
    }


    private fun addTableHeader(table: Table) {
        // --- BARIS 1 ---
        // Menambahkan semua header di baris paling atas.
        table.addHeaderCell(createHeaderCell("NO", 3, 1))
        table.addHeaderCell(createHeaderCell("JAM OPS POMPA", 1, 3))
        table.addHeaderCell(createHeaderCell("SNR", 1, 1))
        table.addHeaderCell(createHeaderCell("SISA", 1, 1))
        table.addHeaderCell(createHeaderCell("SALES", 1, 1))
        table.addHeaderCell(createHeaderCell("JUMLAH", 1, 1))
        table.addHeaderCell(createHeaderCell("HASIL", 1, 1))
        table.addHeaderCell(createHeaderCell("M1\nM2\nM3\nM4", 3, 1))
        table.addHeaderCell(createHeaderCell("TOTALISATOR AWAL", 3, 1))
        table.addHeaderCell(createHeaderCell("TANKI", 3, 1))
        table.addHeaderCell(createHeaderCell("PARAF", 3, 1))

        // --- BARIS 2 ---
        // Menambahkan sub-header di bawah "JAM OPS POMPA"
        table.addHeaderCell(createHeaderCell("HIDUP", 2, 1)) // Rowspan 2
        table.addHeaderCell(createHeaderCell("MATI", 2, 1)) // Rowspan 2
        table.addHeaderCell(createHeaderCell("DURASI", 1, 1)) // Rowspan 1, karena ada (MENIT) di bawahnya
        // Menambahkan sub-header di bawah SNR, SISA, dst.
        table.addHeaderCell(createHeaderCell("NO.", 2, 1)) // Rowspan 2
        table.addHeaderCell(createHeaderCell("DIPPING", 2, 1)) // Rowspan 2
        table.addHeaderCell(createHeaderCell("REF", 2, 1)) // Rowspan 2
        table.addHeaderCell(createHeaderCell("TOPPING", 2, 1)) // Rowspan 2
        table.addHeaderCell(createHeaderCell("DIPSTIK", 2, 1)) // Rowspan 2

        // --- BARIS 3 ---
        // Menambahkan sub-header terakhir di bawah "DURASI".
        table.addHeaderCell(createHeaderCell("(MENIT)", 1, 1))
    }
    private fun populateTableWithData(table: Table, data: List<ToppingUp>) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (data.isEmpty()) {
            // Jika tidak ada data, isi dengan baris kosong
            for (i in 1..15) {
                table.addCell(createDataCell("${i}."))
                for (j in 1..12) { table.addCell(createDataCell("")) }
            }
            // Tambahkan baris total kosong jika tidak ada data
            addTotalRow(table, 0.0)
        } else {
            // Loop untuk mengisi data
            data.forEachIndexed { index, item ->
                table.addCell(createDataCell("${index + 1}."))
                table.addCell(createDataCell(item.start_time?.let { timeFormat.format(Date(it)) } ?: "-"))
                table.addCell(createDataCell(item.end_time?.let { timeFormat.format(Date(it)) } ?: "-"))
                table.addCell(createDataCell(item.duration?.toString() ?: "0"))
                table.addCell(createDataCell(item.snr_no ?: ""))
                table.addCell(createDataCell(item.sisa_dipping?.toString() ?: "0"))
                table.addCell(createDataCell(item.sales_ref?.toString() ?: "0"))
                table.addCell(createDataCell(item.jumlah_topping?.toString() ?: "0"))
                table.addCell(createDataCell(item.hasil_dipstik?.toString() ?: "0.0"))
                table.addCell(createDataCell(item.m_number ?: ""))
                table.addCell(createDataCell(item.totalisator_awal ?: ""))
                table.addCell(createDataCell(item.tanki ?: ""))
                table.addCell(createDataCell(item.operator ?: ""))
            }

            // Hitung total dari 'jumlah_topping'
            val totalTopping = data.sumOf { it.jumlah_topping?.toDouble() ?: 0.0 }

            // Tambahkan baris total ke tabel
            addTotalRow(table, totalTopping)
        }
    }

    // TAMBAHKAN FUNGSI BARU INI DI DALAM KELAS
    private fun addTotalRow(table: Table, total: Double) {
        // ================== PERBAIKAN UTAMA DI SINI ==================
        // Membuat Cell dengan konstruktor Cell(rowspan, colspan)
        val totalLabelCell = Cell(1, 7) // 1 baris, 7 kolom
            .add(Paragraph("JUMLAH"))
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
        table.addCell(totalLabelCell)
        // =============================================================

        table.addCell(createDataCell(total.toString()).setTextAlignment(TextAlignment.CENTER).setBold())

        // ================== PERBAIKAN UTAMA DI SINI ==================
        val emptyCell = Cell(1, 5) // 1 baris, 5 kolom
            .add(Paragraph(""))
        table.addCell(emptyCell)
        // =============================================================
    }

    private fun addFooterContent(document: Document) {
        // Tabel untuk baris pertama (AFT Syamsudin Noor, Tanggal)
        val topFooterTable = Table(UnitValue.createPercentArray(floatArrayOf(5f, 5f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(20f)

        val today = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        val tanggalHariIni = dateFormat.format(today)

        topFooterTable.addCell(createBorderlessCell("AFT SYAMSUDIN NOOR, $tanggalHariIni").setTextAlignment(TextAlignment.LEFT))
        topFooterTable.addCell(createBorderlessCell("").setTextAlignment(TextAlignment.RIGHT))
        document.add(topFooterTable)

        // Tabel utama untuk bagian Tanda Tangan (2 kolom)
        val signatureTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(10f)


        val leftCell = createBorderlessCell("").setTextAlignment(TextAlignment.CENTER)
        leftCell.add(Paragraph("YANG MENERIMA,").setMarginBottom(50f))
        leftCell.add(Paragraph("(......................)").setMarginBottom(2f))
        leftCell.add(Paragraph("AST PP. (ON DUTY)").setUnderline())
        signatureTable.addCell(leftCell)

        // --- Kolom Kanan: YANG MENYERAHKAN ---
        val rightCell = createBorderlessCell("").setTextAlignment(TextAlignment.CENTER)
        rightCell.add(Paragraph("YANG MENYERAHKAN,").setMarginBottom(50f))
        rightCell.add(Paragraph("(......................)").setMarginBottom(2f))
        rightCell.add(Paragraph("AST PP. (ON DUTY)").setUnderline())
        signatureTable.addCell(rightCell)
        document.add(signatureTable)
    }

    private fun createBorderedCell(text: String): Cell {
        return Cell().add(Paragraph(text).setFontSize(9f).setPadding(3f))
            .setBorder(SolidBorder(ColorConstants.BLACK, 0.5f))
    }

    // --- Fungsi-fungsi helper ---
    private fun createHeaderCell(text: String, rowspan: Int = 1, colspan: Int = 1): Cell {
        return Cell(rowspan, colspan).add(Paragraph(text)).setBold().setFontSize(9f)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
    }

    private fun createDataCell(text: String): Cell {
        return Cell().add(Paragraph(text)).setFontSize(9f).setPadding(5f)
    }

    private fun createBorderlessCell(text: String): Cell {
        return Cell().add(Paragraph(text)).setBorder(null).setFontSize(10f)
    }
}