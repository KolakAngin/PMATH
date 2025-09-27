package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class VolumeUnit(override val label: String, override val toBase: Double) : ConversionUnit {
    MM3( "mm³",1e-6),       // 1 mm³ = 1e-6 L
    CM3("1 cm³ (cc)",0.001),      // 1 cm³ (cc) = 1 mL = 0.001 L
    ML("1 mL ",0.001),       // 1 mL = 0.001 L
    LITER("L",1.0),      // 1 L
    M3("1 m³",1000.0),      // 1 m³ = 1000 L
    IN3("cubic inch → L",0.0163871),  // cubic inch → L
    FT3("cubic feet → L",28.3168),    // cubic feet → L
    GAL_US("US gallon → L",3.78541), // US gallon → L
    GAL_UK("UK gallon → L",4.54609), // UK gallon → L
    BARREL("barrel minyak → L",158.987);

    override fun toString() = label
}