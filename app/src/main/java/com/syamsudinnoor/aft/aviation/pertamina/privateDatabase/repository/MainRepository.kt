package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao.MainDao
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlQuality
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalyticsDataVolumeControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.SumOfQualityControlData
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import kotlinx.coroutines.flow.Flow

class MainRepository(private val dao: MainDao) {

    suspend fun insertBridgerQualityControl(bridgerQualityControl: BridgerQualityControl){
        dao.insertBridgerQualityControl(bridgerQualityControl)
    }

    fun getBridgerQualityControl(): Flow<List<BridgerQualityControl>> {
        return dao.getBridgerQualityControl()
    }

    fun getBridgerQualityControlByDate(startTime: Long, endTime: Long): Flow<List<BridgerQualityControl>>{
        return dao.getBridgerQualityControlByDate(startTime, endTime)
    }

    suspend fun deleteBridgerQualityControl(id : Int){
        dao.deleteBridgerQualityControl(id)

    }

    suspend fun insertToppingUp(toppingUp: ToppingUp){
        dao.insertToppingUp(toppingUp)
    }

    fun getToppingUp(): Flow<List<ToppingUp>> {
        return dao.getToppingUp()
    }

    fun getToppingUpByDate(startTime: Long, endTime: Long): Flow<List<ToppingUp>>{
        return dao.getToppingUpByDate(startTime, endTime)
    }

    suspend fun deleteToppingUp(id : Int){
        dao.deleteToppingUp(id)

    }


    suspend fun insertAllAnalisaVolumeControlWithDetail(analisaVolumeControl: AnalisaVolumeControlQuality, details : List<DetailKompartemen>){
        dao.insertVolumeControlWithDetail(analisaVolumeControl, details)
    }

    fun getAllDataVolumeControl() : Flow<List<AnalisaVolumeControlWithDetail>>{
        return dao.getAllDataVolumeControl()
    }

    suspend fun deleteSelectedDataVolume(analisaVolumeControl: AnalisaVolumeControlQuality){
        dao.deleteSelectedDataVolume(analisaVolumeControl)
    }

    fun getAnalisaVolumeControlByDate(startTime: Long, endTime: Long): Flow<List<AnalisaVolumeControlWithDetail>>{
        return dao.getVolumeControlByDate(startTime,endTime)
    }

    suspend fun updateToppingUp(toppingUp: ToppingUp){
        dao.updateToppingUp(toppingUp)
    }

    suspend fun updateBridgerQuality(bridgerQualityControl: BridgerQualityControl){
        dao.updateBridgerQuality(bridgerQualityControl)
    }

    suspend fun updateVolumeControl(analisaVolumeControl: AnalisaVolumeControlQuality, details: List<DetailKompartemen>){
        dao.updateAnalisaWithDetails(analisaVolumeControl,details)
    }

    fun getSumOfVolumeControl(startTime: Long, endTime: Long) : Flow<List<SumOfQualityControlData>>{
        return  dao.getSumQualityControl(startTime,endTime)
    }

    fun getAnalyticsVolumeControl(startTime: Long, endTime: Long) : Flow<List<AnalyticsDataVolumeControl>>{
        return dao.getAnalyticsVolumeControl(startTime,endTime)
    }

}