package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.MainDatabase
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao.MainDao
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
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
}