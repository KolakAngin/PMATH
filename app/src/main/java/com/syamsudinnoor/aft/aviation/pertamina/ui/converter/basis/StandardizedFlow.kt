package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class StandardizedFlow(override val label: String, override val toBase: Double) : ConversionUnit {
    SCFM("SCFM", 0.0283168 * 60 / 3600),  // asumsi ke Nm³/h
    NM3H("Nm³/h", 1.0),
    SM3H("Sm³/h", 1.0);

    override fun toString() = label
}