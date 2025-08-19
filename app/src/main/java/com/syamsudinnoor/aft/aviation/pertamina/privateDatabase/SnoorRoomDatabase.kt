package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao.SNoorDao
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_20
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_21
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_22
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Density_15
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_11

@Database(entities = [Density_15::class, Tangki_9::class, Tangki_10::class, Tangki_11::class,
    Snr_20::class, Snr_21::class, Snr_22::class], version = 1, exportSchema = false)
abstract class SnoorRoomDatabase : RoomDatabase() {
    abstract fun sNoorDao() : SNoorDao

    companion object{
        @Volatile
        private var INSTANCE : SnoorRoomDatabase? = null

        fun getDatabase(context: Context) : SnoorRoomDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }else{
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context,
                        SnoorRoomDatabase::class.java,
                        "aft_syamsudin_noor_utility"
                    ).createFromAsset("databases/aft_syamsudin_noor_utility_3.db").build()
                    INSTANCE = instance
                    return  instance
                }
            }
        }
    }
}