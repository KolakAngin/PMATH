package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class LengthUnit(override val label: String,override val toBase: Double) : ConversionUnit {
    MM("millimeter",0.001),       // millimeter → meter
    CM("centimeter",0.01),        // centimeter → meter
    M("meter",1.0),          // meter
    KM("kilometer",1000.0),      // kilometer
    INCH("inch",0.0254),    // inch
    FOOT("foot",0.3048),    // foot
    YARD("yard",0.9144),    // yard
    MILE("mile",1609.34); // mile

    override fun toString() = label
}
