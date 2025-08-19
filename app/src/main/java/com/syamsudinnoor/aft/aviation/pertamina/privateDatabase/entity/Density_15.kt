package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "density_15",
    indices = [Index(value = ["nomor_data"], name = "ix_density_15_nomor_data", unique = false)]
)
data class Density_15(
    @PrimaryKey
    @ColumnInfo(name = "nomor_data")
    val nomorData: Int,

    @ColumnInfo(name = "density_obsd")
    val densityObsd: Double?,

    @ColumnInfo(name = "temprature")
    val temperature: Double?,

    @ColumnInfo(name = "result")
    val result: Double?
)