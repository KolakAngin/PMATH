package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "user",
    indices = [Index(value = ["nomor_data"], name = "ix_user_nomor_data", unique = false)]
)
data class UserPMATH(
        @PrimaryKey
        @ColumnInfo(name = "nomor_data")
        val nomorData: Int,
        @ColumnInfo(name = "nama")
        val name : String?,
        @ColumnInfo(name = "password")
        val password : String?
)
