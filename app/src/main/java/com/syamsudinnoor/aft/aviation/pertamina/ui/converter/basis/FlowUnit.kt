package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class FlowUnit(val label: String, val toBase: Double) {
    // Basis = m³/s
    LPM("L/min", 1.0 / 1000 / 60),
    GPM_US("GPM (US)", 0.00378541 / 60),
    M3H("m³/h", 1.0 / 3600);

    override fun toString(): String {
        return label
    }
}