package com.syamsudinnoor.aft.aviation.pertamina.utility

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
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AnalisaVolumeControlPdfGenerator {

    fun generatePdf(context: Context, listData: List<AnalisaVolumeControlWithDetail>): Uri? {
        val fileName = "AnalisaVolumeControlReport_${System.currentTimeMillis()}.pdf"
        var pdfUri: Uri? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
                    val document = Document(pdfDocument, PageSize.A4.rotate())
                    buildPdfContent(document, context, listData)
                }
            }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) { downloadsDir.mkdirs() }
            val file = File(downloadsDir, fileName)
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument, PageSize.A4.rotate())
            buildPdfContent(document, context, listData)
            pdfUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }
        return pdfUri
    }

    private fun buildPdfContent(document: Document, context: Context, listData: List<AnalisaVolumeControlWithDetail>) {
        document.setMargins(20f, 20f, 20f, 20f)

        listData.forEachIndexed { index, data ->
            if (index > 0) {
                document.add(AreaBreak())
            }
            addHeaderContent(document, context, data.analisaVolumeControl,index)
            val mainTable = createMainTable(data.detailKompartemen)
            document.add(mainTable)
            addKlaimSection(document, data)
            addFooterContent(document, data.analisaVolumeControl)
        }

        document.close()
    }

    private fun addHeaderContent(document: Document, context: Context, analisa: AnalisaVolumeControlQuality, pageIndex : Int) {

        val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(4f, 6f, 2f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        // --- Kolom Kiri: Dibiarkan kosong untuk keseimbangan ---
        headerTable.addCell(createBorderlessCell(""))

        // --- Kolom Tengah: Judul Dokumen ---
        headerTable.addCell(createBorderlessCell("FORM ANALISA BONGKAR BRIDGER")
            .setBold().setFontSize(14f).setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE))

        // --- Kolom Kanan: Logo Perusahaan ---
        val logoCell = createBorderlessCell("")
            .setHorizontalAlignment(HorizontalAlignment.RIGHT) // Rata kanan
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
        try {
            val drawable = ContextCompat.getDrawable(context, R.drawable.logo_pertamina) // Pastikan nama file logo benar
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageData = ImageDataFactory.create(stream.toByteArray())
            val logo = Image(imageData).scaleToFit(150f, 150f)
            logoCell.add(logo)
        } catch (e: Exception) {
            logoCell.add(Paragraph("LOGO").setFontSize(8f).setTextAlignment(TextAlignment.RIGHT))
        }
        headerTable.addCell(logoCell)
        document.add(headerTable)

        val subHeaderTable = Table(UnitValue.createPercentArray(floatArrayOf(1.5f, 0.2f, 4f, 2.5f, 0.2f, 4f)))
            .setWidth(UnitValue.createPercentValue(100f)).setMarginTop(10f).setFontSize(9f)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        val tanggal = analisa.tanggal?.let { dateFormat.format(Date(it)) } ?: ""

        val ritNumber = pageIndex + 1

        subHeaderTable.addCell(createBorderlessCell("Tanggal"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell(tanggal))
        subHeaderTable.addCell(createBorderlessCell(""))
        subHeaderTable.addCell(createBorderlessCell(""))
        subHeaderTable.addCell(createBorderlessCell("Jam ${TimeConverter.toReadableTime(analisa.tanggal ?: System.currentTimeMillis())}").setTextAlignment(TextAlignment.RIGHT))

        subHeaderTable.addCell(createBorderlessCell("AFT"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell(analisa.aft ?: ""))
        subHeaderTable.addCell(createBorderlessCell("Transportir"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell(analisa.transportir ?: ""))

        subHeaderTable.addCell(createBorderlessCell("Supply Point"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell(analisa.supply_point ?: ""))
        subHeaderTable.addCell(createBorderlessCell("No. Pol Bridger"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell(analisa.no_polisi ?: ""))

        subHeaderTable.addCell(createBorderlessCell("Kuantitas"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell("${formatterNumber(analisa.kuantitas ?: 0)} LITER"))
        subHeaderTable.addCell(createBorderlessCell(""))
        subHeaderTable.addCell(createBorderlessCell(""))
        subHeaderTable.addCell(createBorderlessCell(""))

        subHeaderTable.addCell(createBorderlessCell("Harga Avtur (Incl tax)"))
        subHeaderTable.addCell(createBorderlessCell(":"))
        subHeaderTable.addCell(createBorderlessCell("${formatterNumber(analisa.harga_avtur ?: 0.0)} IDR/liter"))
        subHeaderTable.addCell(createBorderlessCell(""))
        subHeaderTable.addCell(createBorderlessCell(""))
        subHeaderTable.addCell(createBorderlessCell(""))

        document.add(subHeaderTable)
    }

    private fun createMainTable(details: List<DetailKompartemen>): Table {
        val columnWidths = floatArrayOf(2f, 2f, 1f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f)
        val table = Table(UnitValue.createPercentArray(columnWidths))
            .setWidth(UnitValue.createPercentValue(100f)).setMarginTop(10f)
            .setBorder(SolidBorder(ColorConstants.BLACK, 0.5f))

        addTableHeader(table)
        populateTableWithData(table, details)
        return table
    }

    private fun addTableHeader(table: Table) {
        table.addHeaderCell(createHeaderCell("Kompartemen", 2, 1))
        table.addHeaderCell(createHeaderCell("t1\nTera Metrologi (mm)", 2, 1))
        table.addHeaderCell(createHeaderCell("R (mm/l)", 2, 1))
        table.addHeaderCell(createHeaderCell("Ukuran Supply Point (mm)", 2, 1))
        table.addHeaderCell(createHeaderCell("Ukuran DPPU (mm)", 2, 1))
        table.addHeaderCell(createHeaderCell("Selisih ullage", 2, 1))
        table.addHeaderCell(createHeaderCell("Selisih Ltr", 2, 1))
        table.addHeaderCell(createHeaderCell("Density (obs°C)", 2, 1))
        table.addHeaderCell(createHeaderCell("Temp (obs°C)", 2, 1))
        table.addHeaderCell(createHeaderCell("Density 15", 2, 1))
        table.addHeaderCell(createHeaderCell("Corr Factor", 2, 1))
        table.addHeaderCell(createHeaderCell("Liter 15", 2, 1))
    }

    private fun populateTableWithData(table: Table, details: List<DetailKompartemen>) {
        val maxRows = 3
        for (i in 0 until maxRows) {
            if (i < details.size) {
                val item = details[i]
                table.addCell(createDataCell(item.kompartemen ?: ""))
                table.addCell(createDataCell("${formatterNumber(item.tera)}"))
                table.addCell(createDataCell(item.rmm_i?.toString() ?: "0.0"))
                table.addCell(createDataCell(item.ukuran_supply_point?.toString() ?: "0"))
                table.addCell(createDataCell("${formatterNumber(item.ukuran_dppu)}"))
                table.addCell(createDataCell(item.selisih_ullage?.toString() ?: "0"))
                table.addCell(createDataCell(numberFormatter(item.selisih_liter ?: 0.0,4)))
                table.addCell(createDataCell(item.density_obs?.toString() ?: "0.0"))
                table.addCell(createDataCell(item.temp_obs?.toString() ?: "0"))
                table.addCell(createDataCell(numberFormatter(item.density_15 ?: 0.0,4)))
                table.addCell(createDataCell(numberFormatter(item.corr_factor ?: 0.0,4)))
                table.addCell(createDataCell(numberFormatter(item.liter_15 ?: 0.0,4)))
            } else {
                for (j in 1..12) {
                    table.addCell(createDataCell(" "))
                }
            }
        }
    }


    private fun addKlaimSection(document: Document, data: AnalisaVolumeControlWithDetail) {
        val klaimTable = Table(UnitValue.createPercentArray(floatArrayOf(2f, 4f, 1f, 1.5f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setBorder(SolidBorder(ColorConstants.BLACK, 0.5f))

        // --- Kalkulasi Awal ---
        val totalSelisihLiter = data.detailKompartemen.sumOf { it.liter_15 ?: 0.0 }
        val toleransiLosses = (data.analisaVolumeControl.kuantitas ?: 0) * 0.0015
        val hargaAvtur = data.analisaVolumeControl.harga_avtur ?: 0.0

        // --- Logika Perhitungan Klaim ---
        var volumeKlaimLosses = 0.0
        if (totalSelisihLiter < 0 && Math.abs(totalSelisihLiter) > toleransiLosses) {
            volumeKlaimLosses = totalSelisihLiter + toleransiLosses
        }
        val nilaiKlaimLosses = Math.abs(volumeKlaimLosses) * hargaAvtur

        // ================== PANGGIL KONVERTER TERBILANG DI SINI ==================
        val terbilangText = if (nilaiKlaimLosses > 0) {
            TerbilangConverter.toWords(nilaiKlaimLosses)
        } else {
            "TIDAK NILAI KLAIM"
        }
        // =======================================================================

        val klaimLabelCell = Cell(5, 1)
            .add(Paragraph("Perhitungan Klaim").setBold())
            .setTextAlignment(TextAlignment.LEFT)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setBorder(null)
        klaimTable.addCell(klaimLabelCell)

        // Baris Total Selisih Liter
        klaimTable.addCell(createBorderlessCell("Total Selisih Liter", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell("Liter", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell(String.format(Locale.US, "%.2f", totalSelisihLiter), TextAlignment.RIGHT))

        // Baris Toleransi Losses
        klaimTable.addCell(createBorderlessCell("Toleransi Losses sesuai kontrak (0.15% Kuantitas cargo)", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell("Liter", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell(String.format(Locale.US, "%.2f", toleransiLosses), TextAlignment.RIGHT))

        // Baris Volume Klaim Losses
        klaimTable.addCell(createBorderlessCell("Volume Klaim Losses", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell("Liter", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell(String.format(Locale.US, "%.2f", volumeKlaimLosses), TextAlignment.RIGHT))

        // Baris Nilai Klaim Losses
        klaimTable.addCell(createBorderlessCell("Nilai Klaim Losses", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell("IDR", TextAlignment.LEFT))
        klaimTable.addCell(createBorderlessCell(String.format(Locale.US, "%,.2f", nilaiKlaimLosses), TextAlignment.RIGHT))

        // Baris Terbilang (menggunakan hasil konversi)
        klaimTable.addCell(createBorderlessCell("Terbilang", TextAlignment.LEFT))
        val terbilangCell = Cell(1, 2)
            .add(Paragraph(terbilangText).setItalic()) // Dibuat miring agar menonjol
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(null)
        klaimTable.addCell(terbilangCell)

        document.add(klaimTable)
    }

    private fun addFooterContent(document: Document, analisa: AnalisaVolumeControlQuality) {
        // Tabel utama untuk footer, dibagi 3 kolom: Kiri, Tengah (kosong), Kanan
        val footerTable = Table(UnitValue.createPercentArray(floatArrayOf(4f, 1f, 5f))) // Sesuaikan lebar kolom
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(20f)
            .setFontSize(9f)

        // --- KOLOM KIRI: Untuk SPV RSD ---
        val spvCell = createBorderlessCell("").setVerticalAlignment(VerticalAlignment.BOTTOM)
        val spvTable = Table(UnitValue.createPercentArray(1)) // Tabel di dalam sel
            .setTextAlignment(TextAlignment.CENTER)
        spvTable.addCell(createBorderlessCell("Mengetahui,"))
        spvTable.addCell(createBorderlessCell("SPV. RSD"))
        spvTable.addCell(createBorderlessCell(" ").setHeight(50f)) // Spasi untuk TTD
        spvTable.addCell(createBorderlessCell(analisa.spv_rsd ?: "(...................)"))
        spvCell.add(spvTable)
        footerTable.addCell(spvCell)

        // --- KOLOM TENGAH: Dibiarkan kosong untuk memberi jarak ---
        footerTable.addCell(createBorderlessCell(""))

        // --- KOLOM KANAN: Untuk Pengemudi Bridger ---
        val driverCell = createBorderlessCell("")
        // Tabel di dalam sel, dengan 2 kolom untuk pengemudi 1 dan 2
        val driverTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
            .setWidth(UnitValue.createPercentValue(100f))

        // --- Blok Pengemudi 1 (di kolom pertama driverTable) ---
        val driver1Table = Table(UnitValue.createPercentArray(1)).setTextAlignment(TextAlignment.CENTER)
        driver1Table.addCell(createBorderlessCell("Pengemudi Bridger,"))
        driver1Table.addCell(createBorderlessCell(" ").setHeight(50f))
        driver1Table.addCell(createBorderlessCell(analisa.sopir_bridger_1 ?: "(...................)"))
        driverTable.addCell(createBorderlessCell("").add(driver1Table))

        // --- Blok Pengemudi 2 (di kolom kedua driverTable) ---
        val driver2Table = Table(UnitValue.createPercentArray(1)).setTextAlignment(TextAlignment.CENTER)
        driver2Table.addCell(createBorderlessCell("Pengemudi Bridger,"))
        driver2Table.addCell(createBorderlessCell(" ").setHeight(50f))
        driver2Table.addCell(createBorderlessCell(analisa.sopir_bridger_2 ?: "(...................)"))
        driverTable.addCell(createBorderlessCell("").add(driver2Table))

        driverCell.add(driverTable)
        footerTable.addCell(driverCell)

        document.add(footerTable)
    }
    private fun createHeaderCell(text: String, rowspan: Int = 1, colspan: Int = 1): Cell {
        return Cell(rowspan, colspan).add(Paragraph(text).setBold().setFontSize(8f))
            .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setBorder(SolidBorder(ColorConstants.BLACK, 0.5f))
    }
    private fun createDataCell(text: String): Cell {
        return Cell().add(Paragraph(text).setFontSize(8f)).setTextAlignment(TextAlignment.CENTER)
            .setBorder(SolidBorder(ColorConstants.BLACK, 0.5f)).setPadding(3f)
    }
    private fun createBorderlessCell(text: String, alignment: TextAlignment = TextAlignment.LEFT): Cell {
        return Cell().add(Paragraph(text).setTextAlignment(alignment)).setBorder(null)
    }
}