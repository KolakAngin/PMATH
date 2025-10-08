package com.syamsudinnoor.aft.aviation.pertamina.ui.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityCalculatorBinding
import java.text.DecimalFormat

class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListeners()

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title  = "Calculator"
    }

    private fun setupClickListeners() {
        val numberButtons = listOf(binding.b0, binding.b1, binding.b2, binding.b3, binding.b4, binding.b5, binding.b6, binding.b7, binding.b8, binding.b9)
        numberButtons.forEach { logic -> logic.setOnClickListener { onNumberClick(logic.text.toString()) } }

        val operatorButtons = listOf(binding.bplus, binding.bminus, binding.bmul, binding.bdiv)
        operatorButtons.forEach { logic -> logic.setOnClickListener { onOperatorClick(logic.text.toString()) } }

        val functionButtons = listOf(binding.bsin, binding.bcos, binding.btan, binding.blog, binding.bln)
        functionButtons.forEach { logic -> logic.setOnClickListener { onFunctionClick(logic.text.toString()) } }

        binding.bdot.setOnClickListener { onDecimalPointClick() }
        binding.bac.setOnClickListener { onAllClearClick() }
        binding.bc.setOnClickListener { onClearClick() }
        binding.bequal.setOnClickListener { onEqualClick() }
        binding.bbrac1.setOnClickListener { appendToMain("(") }
        binding.bbrac2.setOnClickListener { appendToMain(")") }
        binding.bpi.setOnClickListener { onPiClick() }
        binding.binv.setOnClickListener { appendToMain("^(-1)") }

        // Listener untuk fungsi unary dengan logika baru
        binding.bsquare.setOnClickListener { onUnaryOperation { it * it } } // Kuadrat
        binding.bsqrt.setOnClickListener { onUnaryOperation { Math.sqrt(it) } } // Akar
        binding.bfact.setOnClickListener {
            onUnaryOperation {
                if (it < 0 || it % 1.0 != 0.0) Double.NaN // Faktorial hanya untuk integer non-negatif
                else factorial(it.toLong()).toDouble()
            }
        }
    }

    // =============================================================================================
    // LOGIKA BARU UNTUK FUNGSI UNARY (Kuadrat, Akar, Faktorial)
    // =============================================================================================

    /**
     * Fungsi utama untuk operasi unary.
     * Ia akan mencari angka terakhir di string, menjalakan operasi, lalu merekonstruksi string.
     * @param operation Lambda function untuk operasi matematika (misal: x -> x*x)
     */
    private fun onUnaryOperation(operation: (Double) -> Double) {
        val expression = binding.idTVprimary.text.toString()
        if (expression.isEmpty()) return

        // 1. Cari range (indeks awal dan akhir) dari angka terakhir
        val lastNumberRange = findLastNumberRange(expression)
        if (lastNumberRange == null) {
            showErrorToast("Tidak ada angka untuk dioperasikan")
            return
        }

        // 2. Ekstrak bagian sebelum angka dan angka itu sendiri
        val prefix = expression.substring(0, lastNumberRange.first)
        val numberString = expression.substring(lastNumberRange)

        try {
            // 3. Konversi angka, lakukan operasi, dan format hasilnya
            val number = numberString.toDouble()
            val result = operation(number)

            if (result.isNaN()) {
                Toast.makeText(this, "Input Tidak Valid Untuk Operasi Ini", Toast.LENGTH_SHORT).show()
                return
            }

            val resultString = formatNumber(result)

            // 4. Gabungkan kembali string dan tampilkan
            binding.idTVprimary.text = "$prefix$resultString"
            binding.idTVSecondary.text = expression // Tampilkan ekspresi asli di baris sekunder

        } catch (e: NumberFormatException) {
            showErrorToast("Format angka tidak valid")
        }
    }

    /**
     * Helper function untuk menemukan indeks awal dan akhir dari angka terakhir dalam string.
     * Contoh: "12+34.5" -> akan mengembalikan range untuk "34.5"
     */
    private fun findLastNumberRange(expression: String): IntRange? {
        var endIndex = -1
        var startIndex = -1

        // Cari dari belakang, temukan karakter yang merupakan bagian dari angka
        for (i in expression.indices.reversed()) {
            if (expression[i].isDigit() || expression[i] == '.') {
                endIndex = i
                break
            }
        }

        if (endIndex == -1) return null // Tidak ada angka sama sekali

        // Dari digit terakhir, cari ke depan sampai bukan bagian dari angka
        for (i in endIndex downTo 0) {
            if (expression[i].isDigit() || expression[i] == '.') {
                startIndex = i
            } else {
                break
            }
        }

        // Cek kasus unary minus, misal pada "5x-2", angka terakhir adalah "-2"
        if (startIndex > 0 && expression[startIndex-1] == '-') {
            val prevChar = if(startIndex > 1) expression[startIndex-2] else null
            // Cek apakah minus didahului oleh operator lain atau kurung buka
            if(prevChar == null || "+-x/(".contains(prevChar)){
                startIndex--
            }
        }

        return if (startIndex != -1) IntRange(startIndex, endIndex) else null
    }

    // =============================================================================================
    // FUNGSI ONCLICK LAINNYA
    // =============================================================================================

    private fun appendToMain(text: String) { binding.idTVprimary.append(text) }
    private fun onNumberClick(number: String) { appendToMain(number) }
    private fun onAllClearClick() {
        binding.idTVprimary.text = ""
        binding.idTVSecondary.text = ""
    }
    private fun onClearClick() {
        val currentText = binding.idTVprimary.text.toString()
        if (currentText.isNotEmpty()) {
            binding.idTVprimary.text = currentText.dropLast(1)
        }
    }
    private fun onOperatorClick(operator: String) {
        val currentText = binding.idTVprimary.text.toString()
        if (currentText.isNotEmpty() && !isLastCharOperator(currentText)) {
            appendToMain(operator)
        }
    }
    private fun onFunctionClick(func: String) { appendToMain("$func(") }
    private fun onPiClick() { appendToMain("3.141") }
    private fun onDecimalPointClick() { appendToMain(".") }


    private fun onEqualClick() {
        val expression = binding.idTVprimary.text.toString()
        if (expression.isNotEmpty()) {
            try {
                // SOLUSI: Ganti semua koma (,) dengan titik (.) sebelum evaluasi
                val sanitizedExpression = expression.replace(',', '.')

                val result = evaluate(sanitizedExpression) // <--- Hitung string yang sudah bersih
                binding.idTVprimary.text = formatNumber(result)
                binding.idTVSecondary.text = expression
            } catch (e: Exception) {
                showErrorToast(e.message ?: "Ekspresi Tidak Valid")
            }
        }
    }

    // =============================================================================================
    // FUNGSI HELPER & EVALUATE YANG LEBIH ROBUST
    // =============================================================================================

    private fun factorial(n: Long): Long {
        if (n < 0 || n > 20) return -1 // Batasi input untuk menghindari overflow
        if (n == 0L || n == 1L) return 1
        var result = 1L
        for (i in 2..n) { result *= i }
        return result
    }

    private fun formatNumber(number: Double): String {
        return if (number.isInfinite() || number.isNaN()) "Error"
        else if (number % 1.0 == 0.0 && number < Long.MAX_VALUE) number.toLong().toString()
        else DecimalFormat("#.##########").format(number)
    }

    private fun isLastCharOperator(text: String): Boolean {
        if (text.isEmpty()) return false
        return "+-x/".contains(text.last())
    }

    private fun showErrorToast(message: String = "Argumen Tidak Valid") {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Fungsi evaluate yang telah diperbaiki untuk menangani ekspresi bersarang dengan benar.
     */
    fun evaluate(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() { ch = if (++pos < str.length) str[pos].code else -1 }
            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Karakter tidak terduga: ${ch.toChar()}")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('x'.code) || eat('×'.code) -> x *= parseFactor()
                        eat('/'.code)  || eat('÷'.code)-> {
                            val divisor = parseFactor()
                            if (divisor == 0.0) throw ArithmeticException("Tidak bisa dibagi dengan nol")
                            x /= divisor
                        }
                        ch == '('.code || Character.isLetter(ch) -> x *= parseFactor() // Perkalian implisit
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return +parseFactor()
                if (eat('-'.code)) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('('.code)) {
                    x = parseExpression()
                    if (!eat(')'.code)) throw RuntimeException("Kurung tutup ')' tidak ditemukan")
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (Character.isLetter(ch)) {
                    while (Character.isLetter(ch)) nextChar()
                    val func = str.substring(startPos, pos)
                    x = parseFactor()
                    x = when (func) {
                        "sqrt" -> Math.sqrt(x)
                        "sin" -> Math.sin(Math.toRadians(x))
                        "cos" -> Math.cos(Math.toRadians(x))
                        "tan" -> Math.tan(Math.toRadians(x))
                        "log" -> Math.log10(x)
                        "ln" -> Math.log(x)
                        else -> throw RuntimeException("Fungsi tidak dikenal: $func")
                    }
                } else {
                    throw RuntimeException("Ekspresi tidak terduga: ${ch.toChar()}")
                }
                if (eat('^'.code)) x = Math.pow(x, parseFactor())
                return x
            }
        }.parse()
    }
}

