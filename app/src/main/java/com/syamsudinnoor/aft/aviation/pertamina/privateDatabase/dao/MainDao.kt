package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalyticsDataVolumeControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.SumOfQualityControlData
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

    @Query("SELECT * FROM topping_up WHERE start_time BETWEEN :startTime AND :endTime ORDER BY start_time DESC")
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

    @Query("SELECT * FROM analisa_volume_control_quality order by tanggal desc")
    fun getAllDataVolumeControl(): Flow<List<AnalisaVolumeControlWithDetail>>

    @Delete
    suspend fun deleteSelectedDataVolume(analisaVolumeControl: AnalisaVolumeControlQuality)

    @Query("SELECT * FROM analisa_volume_control_quality WHERE tanggal BETWEEN :startTime AND :endTime")
    fun getVolumeControlByDate(startTime: Long, endTime: Long): Flow<List<AnalisaVolumeControlWithDetail>>


    @Update
    suspend fun updateToppingUp(toppingUp: ToppingUp)

    @Update
    suspend fun updateBridgerQuality(bridgerQualityControl: BridgerQualityControl)

    @Update
    suspend fun updateVolumeControl(analisaVolumeControl: AnalisaVolumeControlQuality)

    @Query("DELETE FROM volume_control_quality_detail WHERE idAnalisa = :idAnalisaInduk")
    suspend fun deleteDetailsByAnalisaId(idAnalisaInduk: Int)

    @Transaction
    suspend fun updateAnalisaWithDetails(analisa: AnalisaVolumeControlQuality, details: List<DetailKompartemen>) {
        // Langkah 1: Update data induk
        updateVolumeControl(analisa)

        // Langkah 2: Hapus semua data anak yang lama
        deleteDetailsByAnalisaId(analisa.idAnalisa)

        // Langkah 3: Siapkan dan insert data anak yang baru
        // (Penting: pastikan ID induknya sudah benar)
        details.forEach { it.idAnalisa = analisa.idAnalisa }
        insertAllDetialKompartemen(details)
    }


    @Query("Select tanggal, SUM(kuantitas) as total_kuantitas from analisa_volume_control_quality  where tanggal BETWEEN :startTime AND :endTime group by date(tanggal/1000,'unixepoch','localtime')")
    fun getSumQualityControl(startTime: Long, endTime: Long) : Flow<List<SumOfQualityControlData>>


    @Query(
        "SELECT a.tanggal as Tanggal, sum(d.liter_15) as Selisih, count(distinct a.idAnalisa) as Total_Bridger from analisa_volume_control_quality a join volume_control_quality_detail d on a.idAnalisa = d.idAnalisa where a.tanggal BETWEEN :startTime AND :endTime group by date(a.tanggal/1000,'unixepoch','localtime')"
    )
    fun getAnalyticsVolumeControl(startTime: Long, endTime: Long) : Flow<List<AnalyticsDataVolumeControl>>
}