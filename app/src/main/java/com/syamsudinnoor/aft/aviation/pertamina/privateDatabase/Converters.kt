package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(value : Date?) : Long?{
        return value?.time
    }
}