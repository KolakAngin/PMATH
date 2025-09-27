package com.syamsudinnoor.aft.aviation.pertamina.ui.converter.basis

enum class PressureUnit(override val label: String,override val toBase: Double) : ConversionUnit {
    PA("Pascal",1.0),                  // Pascal
    KPA("kilopascal",1000.0),              // kilopascal
    MPA("megapascal",1_000_000.0),         // megapascal
    BAR("bar",100_000.0),           // bar
    MBAR("milibar",100.0),              // millibar
    ATM("atmosfer standar",101_325.0),           // atmosfer standar
    TORR("Torr",133.322),            // Torr
    MMHG("mmHg",133.322),            // mmHg (sama dgn Torr)
    INHG("inchi of mercury",3386.39),            // inch of mercury
    PSI("pound per square inch (PSI",14.689476),             // pound per square inch
    KGFCM2("kilogram-force per cm²",98_066.5),         // kilogram-force per cm²
    MMH2O("millimeter of water column",9.80665),           // millimeter of water column
    INH2O("inch of water column",249.089);    // inch of water column

    override fun toString() = label
}