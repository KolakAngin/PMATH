package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class MassFlow(override val label: String, override val toBase: Double) : ConversionUnit {
    KGS("kg/s", 1.0),
    KGH("kg/h", 1.0 / 3600),
    LBMIN("lb/min", 0.453592 / 60),
    LBH("lb/h", 0.453592 / 3600);

    override fun toString() = label
}