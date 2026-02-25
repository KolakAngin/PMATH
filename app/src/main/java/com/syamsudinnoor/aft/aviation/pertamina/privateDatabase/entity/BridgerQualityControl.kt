package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class BridgerQualityControl(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "time")
    val dateTime : Long?,

    @ColumnInfo(name = "bridger_no")
    val bridger_no : String?,

    @ColumnInfo(name = "bpp")
    val bpp : String?,

    @ColumnInfo(name = "volume_liter")
    val volume_liter : Double?,

    @ColumnInfo(name = "seal")
    val seal : String?,

    @ColumnInfo(name = "test_report_no")
    val test_report_no : String?,

    @ColumnInfo(name = "density_15_from_distributor")
    val density_15_from_distributor : Double?,


    @ColumnInfo(name = "afrn_no")
    val afrn_no : String?,

    @ColumnInfo(name = "density_obsd_rec_document")
    val density_obsd_rec_document : Double?,


    @ColumnInfo(name = "temp_rec_document")
    val temp_rec_document : Double?,

    @ColumnInfo(name = "density_15_result_rec_document")
    val density_15_result_rec_document : Double?,

    @ColumnInfo(name = "density_obsd")
    val density_obsd : Double?,


    @ColumnInfo(name = "temprature")
    val temprature : Double?,

    @ColumnInfo(name = "density_15_calculation")
    val density_15_calculation : Double?,

    @ColumnInfo(name = "diff_from_density_distributor")
    val diff_from_density_distributor : Double?,


    @ColumnInfo(name = "app_star")
    val app_star : String?,

    @ColumnInfo(name = "cu_psm")
    val cu_psm : Double?,

    @ColumnInfo(name = "tangki")
    val tangki : String?,

    @ColumnInfo(name = "operator_name")
    val operator_name : String?,

    @ColumnInfo(name = "catatan")
    val catatan : String?

) : Parcelable