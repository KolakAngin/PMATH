package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "analisa_volume_control_quality")
@Parcelize
data class AnalisaVolumeControlQuality(

    @PrimaryKey(autoGenerate = true)
    var idAnalisa : Int = 0,
    var tanggal : Long?,
    var aft : String?,
    var supply_point :  String?,
    var transportir : String?,
    var no_polisi : String?,
    var kuantitas : Int?,
    var harga_avtur : Double?,
    var spv_rsd : String?,
    var sopir_bridger_1 : String?,
    var sopir_bridger_2 : String?

) : Parcelable
