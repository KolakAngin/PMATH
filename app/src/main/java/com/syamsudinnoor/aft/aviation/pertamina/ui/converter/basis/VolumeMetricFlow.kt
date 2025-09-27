package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class VolumetricFlow(override val label: String, override val toBase: Double) : ConversionUnit {
    LPM("L/min", 1.0 / 1000 / 60),
    LPS("L/s", 1.0 / 1000),
    M3H("m³/h", 1.0 / 3600),
    M3S("m³/s", 1.0),
    GPM_US("GPM (US)", 0.00378541 / 60),
    GPM_UK("GPM (UK)", 0.00454609 / 60),
    CFM("CFM", 0.0283168 / 60),
    CFS("cfs", 0.0283168),
    MGD("MGD", 3.78541e6 / 86400);

    override fun toString() = label
}