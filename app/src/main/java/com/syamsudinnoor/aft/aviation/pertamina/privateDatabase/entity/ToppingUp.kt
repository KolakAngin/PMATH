package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "topping_up")
@Parcelize
data class ToppingUp(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Int = 0,

    @ColumnInfo(name = "start_time")
    val start_time : Long?,

    @ColumnInfo(name = "end_time")
    val end_time : Long?,


    @ColumnInfo(name = "duration")
    val duration : Int?,

    @ColumnInfo(name = "snr_no")
    val snr_no : String?,

    @ColumnInfo("sisa_dipping")
    val sisa_dipping : Int?,

    @ColumnInfo("sales_ref")
    val sales_ref : Int?,

    @ColumnInfo("jumlah_topping")
    val jumlah_topping : Int?,

    @ColumnInfo("hasil_dipstik")
    val hasil_dipstik: Double?,

    @ColumnInfo("m_number")
    val m_number : String?,

    @ColumnInfo("totalisator_awal")
    val totalisator_awal : String?,

    @ColumnInfo("totalisator_akhir")
    val totalisator_akhir : String?,

    @ColumnInfo("tanki")
    val tanki : String?,

    @ColumnInfo("operator")
    val operator : String?
): Parcelable