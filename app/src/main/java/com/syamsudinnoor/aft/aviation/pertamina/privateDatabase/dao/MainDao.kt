package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
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


    @Query("SELECT * FROM topping_up ORDER BY start_time DESC")
    fun getToppingUp(): Flow<List<ToppingUp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToppingUp(toppingUp: ToppingUp)


    @Query("DELETE FROM topping_up where id = :id")
    suspend fun deleteToppingUp(id : Int)

    @Query("SELECT * FROM topping_up WHERE start_time BETWEEN :startTime AND :endTime")
    fun getToppingUpByDate(startTime: Long, endTime: Long): Flow<List<ToppingUp>>


    @Insert
    suspend fun insertVolumeControl(analisaVolumeControl: AnalisaVolumeControlQuality) : Long

    @Insert
    suspend fun insertAllDetialKompartemen(details : List<DetailKompartemen>)


    @Transaction
    suspend fun insertVolumeControlWithDetail(analisaVolumeControl: AnalisaVolumeControlQuality, details : List<DetailKompartemen>){
        val idInduk = insertVolumeControl(analisaVolumeControl)
        details.forEach { detailAnak ->
            detailAnak.idAnalisa = idInduk.toInt()
        }
        insertAllDetialKompartemen(details)
    }

    @Query("SELECT * FROM analisa_volume_control_quality order by tanggal asc")
    fun getAllDataVolumeControl(): Flow<List<AnalisaVolumeControlWithDetail>>

    @Delete
    suspend fun deleteSelectedDataVolume(analisaVolumeControl: AnalisaVolumeControlQuality)

    @Query("SELECT * FROM analisa_volume_control_quality WHERE tanggal BETWEEN :startTime AND :endTime")
    fun getVolumeControlByDate(startTime: Long, endTime: Long): Flow<List<AnalisaVolumeControlWithDetail>>

}