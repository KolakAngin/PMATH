package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao.MainDao
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl


@Database(entities = [BridgerQualityControl::class], version = 1)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase(){
    abstract fun getDao(): MainDao

    companion object{
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
             return tempInstance
            }else{
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "main_database").build()

                    INSTANCE = instance
                    return instance
                }
            }

        }
    }
}