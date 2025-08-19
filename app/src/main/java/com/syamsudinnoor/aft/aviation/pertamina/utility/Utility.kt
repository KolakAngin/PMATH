package com.syamsudinnoor.aft.aviation.pertamina.utility


import java.math.BigDecimal
import java.text.NumberFormat


fun numberFormatter(number: Double): String{
    val roundingDecimal = BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP)
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(roundingDecimal)
}

fun numberFormatter(number: Int): String{
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(number)
}