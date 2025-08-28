package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import kotlinx.coroutines.flow.Flow


@Dao
interface MainDao {


    @Query("SELECT * FROM BridgerQualityControl ORDER BY time DESC")
    fun getBridgerQualityControl(): Flow<List<BridgerQualityControl>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBridgerQualityControl(bridgerQualityControl: BridgerQualityControl)


    @Query("DELETE FROM BridgerQualityControl where id = :id")
    suspend fun deleteBridgerQualityControl(id : Int)

    @Query("SELECT * FROM BridgerQualityControl WHERE time BETWEEN :startTime AND :endTime")
    fun getBridgerQualityControlByDate(startTime: Long, endTime: Long): Flow<List<BridgerQualityControl>>




}