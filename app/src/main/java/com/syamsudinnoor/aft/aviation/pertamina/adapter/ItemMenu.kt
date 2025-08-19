package com.syamsudinnoor.aft.aviation.pertamina.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ItemMenu(
    val name : String,
    val icon : Int
) : Parcelable
