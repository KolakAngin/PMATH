package com.syamsudinnoor.aft.aviation.pertamina.utility

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.properties.HorizontalAlignment
import java.io.ByteArrayOutputStream



class BridgerQualityPDFGenerator {

    fun generatePdf(context: Context, data: List<BridgerQualityControl>): Uri? {
        val fileName = "BridgerQualityControlReport_${System.currentTimeMillis()}.pdf"
        var pdfUri: Uri? = null

        // Metode penyimpanan berbeda tergantung versi Android
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
                    val document = Document(pdfDocument, PageSize.A4.rotate())
                    // Memanggil semua fungsi untuk membangun PDF
                    buildPdfContent(document, context, data)
                }
            }
        } else { // Android 9 ke bawah
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument, PageSize.A4.rotate())
            // Memanggil semua fungsi untuk membangun PDF
            buildPdfContent(document, context, data)
            pdfUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }
        return pdfUri
    }

    private fun buildPdfContent(document: Document, context: Context, data: List<BridgerQualityControl>) {
        document.setMargins(20f, 20f, 20f, 10f)
        addHeaderContent(document, context)
        val mainTable = createDataTable(data)
        document.add(mainTable)
        addFooterContent(document)
        document.close()
    }

    private fun createDataTable(data: List<BridgerQualityControl>): Table {
        val columnWidths = floatArrayOf(
            1f, 2f, 2.5f, 2.5f, 2f, 2.5f, 3f, 2.5f, 3f, 2f, 2f, 2.5f, 2f, 2f, 2.5f, 3f, 1.5f, 2f, 2.5f, 3f
        )
        val table = Table(UnitValue.createPercentArray(columnWidths))
            .setWidth(UnitValue.createPercentValue(100f))
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(10f)
            .setFixedLayout()

        addTableHeader(table)
        populateTableWithData(table, data)
        return table
    }


    private fun addTableHeader(table: Table) {
        // Baris Header 1 (Header Paling Atas)
        table.addHeaderCell(createHeaderCell("", 1, 6).setBorder(null)) // Kosong untuk grup 1
        table.addHeaderCell(createHeaderCell("TANK BATCH DOCUMENT", 1, 2))
        table.addHeaderCell(createHeaderCell("RECEIVING DOCUMENT", 1, 4))
        table.addHeaderCell(createHeaderCell("CONTROL CHECK", 1, 3))
        table.addHeaderCell(createHeaderCell("", 1, 3).setBorder(null)) // Kosong untuk grup 5
        table.addHeaderCell(createHeaderCell("PRODUCT RECEIVED (SPV RECEIVING)", 2, 2))

        // Baris Header 2 (Nama Kolom)
        // Grup 1
        table.addHeaderCell(createHeaderCell("NO", 2, 1))
        table.addHeaderCell(createHeaderCell("TIME", 2, 1))
        table.addHeaderCell(createHeaderCell("BRIDGER NO", 2, 1))
        table.addHeaderCell(createHeaderCell("BPP NO", 2, 1))
        table.addHeaderCell(createHeaderCell("VOLUME (LITER)", 2, 1))
        table.addHeaderCell(createHeaderCell("SEAL / SEGEL", 2, 1))

        // Grup 2
        table.addHeaderCell(createHeaderCell("TEST REPORT NO", 2, 1))
        table.addHeaderCell(createHeaderCell("DENS 15 C", 2, 1))

        // Grup 3
        table.addHeaderCell(createHeaderCell("AFRN NO", 2, 1))
        table.addHeaderCell(createHeaderCell("DENS' OBSVD", 2, 1))
        table.addHeaderCell(createHeaderCell("TEMP", 2, 1))
        table.addHeaderCell(createHeaderCell("DENS' 15 C", 2, 1))

        // Grup 4
        table.addHeaderCell(createHeaderCell("DENS' OBSVD", 2, 1))
        table.addHeaderCell(createHeaderCell("TEMP C", 2, 1))
        table.addHeaderCell(createHeaderCell("DENS 15 C", 2, 1))

        // Grup 5
        table.addHeaderCell(createHeaderCell("DIFF. (max 0,003 kg/l)*", 2, 1))
        table.addHeaderCell(createHeaderCell("APP**", 2, 1))
        table.addHeaderCell(createHeaderCell("CU (pS/m)", 2, 1))

        // Baris Header 3 (Sub-header untuk "PRODUCT RECEIVED")
        table.addHeaderCell(createHeaderCell("TANKI", 1, 1))
        table.addHeaderCell(createHeaderCell("NAME & SIGN", 1, 1))
    }

    private fun populateTableWithData(table: Table, data: List<BridgerQualityControl>) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        data.forEachIndexed { index, item ->
            // Grup 1
            table.addCell(createDataCell((index + 1).toString()))
            table.addCell(createDataCell(item.dateTime?.let { timeFormat.format(Date(it)) } ?: "-"))
            table.addCell(createDataCell(item.bridger_no ?: ""))
            table.addCell(createDataCell(item.bpp ?: ""))
            table.addCell(createDataCell(item.volume_liter?.toInt().toString()))
            table.addCell(createDataCell(item.seal ?: ""))

            // Grup 2
            table.addCell(createDataCell(item.test_report_no ?: ""))
            table.addCell(createDataCell(item.density_15_from_distributor?.toString() ?: ""))

            // Grup 3
            table.addCell(createDataCell(item.afrn_no ?: ""))
            table.addCell(createDataCell(item.density_obsd_rec_document?.toString() ?: ""))
            table.addCell(createDataCell(item.temp_rec_document?.toString() ?: ""))
            table.addCell(createDataCell(item.density_15_result_rec_document?.toString() ?: ""))

            // Grup 4
            table.addCell(createDataCell(item.density_obsd?.toString() ?: ""))
            table.addCell(createDataCell(item.temprature?.toString() ?: ""))
            table.addCell(createDataCell(item.density_15_calculation?.toString() ?: ""))

            // Grup 5
            table.addCell(createDataCell(item.diff_from_density_distributor?.toString() ?: ""))
            table.addCell(createDataCell(item.app_star ?: ""))
            table.addCell(createDataCell(item.cu_psm?.toString() ?: ""))

            // Grup 6
            table.addCell(createDataCell(item.tangki ?: ""))
            table.addCell(createDataCell(item.operator_name ?: ""))
        }
    }

    private fun addHeaderContent(document: Document, context: Context) {
        // 1. Tambahkan Judul Utama di paling atas dan tengah
        document.add(
            Paragraph("BRIDGER QUALITY CONTROL BEFORE RECEIPT RECORD")
                .setBold().setFontSize(14f).setTextAlignment(TextAlignment.CENTER)
        );

        // 2. Buat tabel untuk menampung blok Info (kiri) dan blok Logo (kanan)
        val infoAndLogoTable = Table(UnitValue.createPercentArray(floatArrayOf(7.5f, 2.5f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(5f); // Beri sedikit jarak dari judul utama

        // --- KOLOM KIRI (Info AFT/DATE/GRADE) ---
        val leftCell = createBorderlessCell("");
        val infoTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 0.2f, 5f)))
            .setWidth(UnitValue.createPercentValue(100f));
        val reportDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date());

        infoTable.addCell(createBorderlessCell("AFT"));
        infoTable.addCell(createBorderlessCell(":"));
        infoTable.addCell(createBorderlessCell("SYAMSUDIN NOOR"));
        infoTable.addCell(createBorderlessCell("DATE"));
        infoTable.addCell(createBorderlessCell(":"));
        infoTable.addCell(createBorderlessCell(reportDate));
        infoTable.addCell(createBorderlessCell("GRADE"));
        infoTable.addCell(createBorderlessCell(":"));
        infoTable.addCell(createBorderlessCell("JET A-1"));

        leftCell.add(infoTable);
        infoAndLogoTable.addCell(leftCell);

        // --- KOLOM KANAN (Logo dan Teks Referensi) ---
        val rightCell = createBorderlessCell("").setTextAlignment(TextAlignment.RIGHT);

        // Menambahkan Logo
        try {
            val drawable = ContextCompat.getDrawable(context, R.drawable.logo_pertamina); // PASTIKAN NAMA FILE LOGO BENAR
            val bitmap = (drawable as BitmapDrawable).bitmap;
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            val imageData = ImageDataFactory.create(stream.toByteArray());
            val logo = Image(imageData).scaleToFit(100f, 100f).setHorizontalAlignment(
                HorizontalAlignment.RIGHT)
            rightCell.add(logo);
        } catch (e: Exception) {
            rightCell.add(Paragraph(" ").setMarginBottom(60f)); // Beri ruang kosong jika logo gagal dimuat
            e.printStackTrace();
        }

        // Menambahkan Teks Referensi di bawah Logo
        rightCell.add(Paragraph("SF-103/2012 - AVS Rev.1").setFontSize(9f)).setTextAlignment(TextAlignment.RIGHT)

        infoAndLogoTable.addCell(rightCell.setVerticalAlignment(VerticalAlignment.TOP));

        document.add(infoAndLogoTable);
    }


    private fun addFooterContent(document: Document) {
        document.add(Paragraph("REMARK...").setBold().setMarginTop(10f))

        // Tabel utama untuk footer, dibagi 2 kolom: Penjelasan dan Persetujuan
        val footerTable = Table(UnitValue.createPercentArray(floatArrayOf(6.5f, 3.5f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(5f)

        // Kolom Kiri: Penjelasan
        val leftCell = createBorderlessCell("").setTextAlignment(TextAlignment.LEFT)
        leftCell.add(Paragraph("APP** Visual check appearance (Clear & Bright)").setFontSize(8f))
        leftCell.add(Paragraph("CU pS/m, Conductivity Unit (pico Siemens/meter)").setFontSize(8f))
        leftCell.add(Paragraph("*) Difference max 0.003 Kg/l").setFontSize(8f))
        leftCell.add(Paragraph("If more than one bridger, the density of the first bridger is the reference").setFontSize(8f))
        footerTable.addCell(leftCell)

        // Kolom Kanan: Persetujuan
        val rightCell = createBorderlessCell("").setTextAlignment(TextAlignment.LEFT)
        rightCell.add(Paragraph("Prepared by,").setFontSize(9f))
        rightCell.add(Paragraph("Checked by,").setFontSize(9f).setMarginTop(10f))
        rightCell.add(Paragraph("Approved by,").setFontSize(9f).setMarginTop(10f))
        footerTable.addCell(rightCell.setVerticalAlignment(VerticalAlignment.BOTTOM))

        document.add(footerTable)

        // Tabel untuk Nama dan Jabatan di bawahnya
        val nameTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f, 1f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(10f)

        // Baris Jabatan
        nameTable.addCell(createBorderlessCell("QC Bridger").setTextAlignment(TextAlignment.CENTER))
        nameTable.addCell(createBorderlessCell("SPV. Aviobridge").setTextAlignment(TextAlignment.CENTER))
        nameTable.addCell(createBorderlessCell("Operator").setTextAlignment(TextAlignment.CENTER))

        // Baris kosong untuk area tanda tangan
        nameTable.addCell(createBorderlessCell(" ").setHeight(50f))
        nameTable.addCell(createBorderlessCell(" ").setHeight(50f))
        nameTable.addCell(createBorderlessCell(" ").setHeight(50f))

        // Baris untuk placeholder nama
        nameTable.addCell(createBorderlessCell("(......................)").setTextAlignment(TextAlignment.CENTER))
        nameTable.addCell(createBorderlessCell("(......................)").setTextAlignment(TextAlignment.CENTER))
        nameTable.addCell(createBorderlessCell("(......................)").setTextAlignment(TextAlignment.CENTER))

        document.add(nameTable)
    }



    // --- Fungsi-fungsi helper (tidak ada perubahan) ---
    private fun createHeaderCell(text: String, rowspan: Int = 1, colspan: Int = 1): Cell {
        return Cell(rowspan, colspan).add(Paragraph(text)).setBold().setFontSize(7f) // Font sedikit dikecilkan
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
    }

    private fun createDataCell(text: String): Cell {
        return Cell().add(Paragraph(text)).setFontSize(8f).setPadding(2f)
    }

    private fun createBorderlessCell(text: String): Cell {
        return Cell().add(Paragraph(text)).setBorder(null).setFontSize(10f)
    }
}