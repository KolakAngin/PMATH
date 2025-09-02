package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "corr_factor",indices = [Index(value = ["nomor_data"], name = "ix_density_15_nomor_data", unique = false)])
@Parcelize
data class CorrFactor(
    @PrimaryKey
    @ColumnInfo(name = "nomor_data")
    val nomorData: Int,

    @ColumnInfo(name = "density_real")
    val densityReal: String?,

    @ColumnInfo(name = "density")
    val density : Double?,

    @ColumnInfo("temperature")
    val temperature : Int?,

    @ColumnInfo("density_15")
    val density15 : Double?,

    @ColumnInfo("correction_factor")
    val correctionFactor : Double?


) : Parcelable