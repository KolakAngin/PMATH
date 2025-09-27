package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalisaVolumeControlWithDetail(
    @Embedded val analisaVolumeControl: AnalisaVolumeControlQuality,
    @Relation(
        parentColumn = "idAnalisa",
        entityColumn = "idAnalisa"
    )
    val detailKompartemen: List<DetailKompartemen>
) : Parcelable
