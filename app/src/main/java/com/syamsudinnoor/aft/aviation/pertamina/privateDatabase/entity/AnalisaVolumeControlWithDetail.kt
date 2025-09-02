package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import androidx.room.Embedded
import androidx.room.Relation


data class AnalisaVolumeControlWithDetail(
    @Embedded val analisaVolumeControl: AnalisaVolumeControlQuality,
    @Relation(
        parentColumn = "idAnalisa",
        entityColumn = "idAnalisa"
    )
    val detailKompartemen: List<DetailKompartemen>
)
