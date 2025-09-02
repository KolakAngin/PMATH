package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "volume_control_quality_detail",
    foreignKeys = [
        ForeignKey(entity = AnalisaVolumeControlQuality::class,
            ["idAnalisa"],["idAnalisa"],
            onDelete = ForeignKey.CASCADE)
    ])
data class DetailKompartemen(
    @PrimaryKey(autoGenerate = true)
    var idDetail : Int = 0,
    var idAnalisa : Int,

    var kompartemen : String?,
    var tera : Int?,
    var ukuran_supply_point : Int?,
    var rmm_i : Double?,
    var ukuran_dppu : Int?,
    var density_obs : Double?,
    var temp_obs : Int?,
    var selisih_ullage : Int?,
    var selisih_liter : Double?,
    var corr_factor : Double?,
    var density_15 : Double?,
    var liter_15 : Double?
)
