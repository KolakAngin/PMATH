package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tangki_11",[Index(value = ["nomor_data"], name = "ix_tangki_11_nomor_data", unique = false)])
@Parcelize
data class Tangki_11(
    @PrimaryKey
    @ColumnInfo(name = "nomor_data")
    val nomorData: Int, // Tipe Int karena NOT NULL

    @ColumnInfo(name = "cm")
    val cm: Double?,

    @ColumnInfo(name = "mm")
    val mm: Int?,

    @ColumnInfo(name = "liter")
    val liter: Double?
) : Parcelable
