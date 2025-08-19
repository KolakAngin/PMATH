package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "tangki_9",
    indices = [Index(value = ["nomor_data"], name = "ix_tangki_9_nomor_data", unique = false)]
)
data class Tangki_9(
    @PrimaryKey
    @ColumnInfo(name = "nomor_data")
    val nomorData: Int,

    @ColumnInfo(name = "cm")
    val cm: Double?,

    // PERBAIKAN UTAMA: Tipe data diubah dari Int? menjadi Double? agar cocok dengan REAL
    @ColumnInfo(name = "mm")
    val mm: Double?,

    @ColumnInfo(name = "liter")
    val liter: Double?
)