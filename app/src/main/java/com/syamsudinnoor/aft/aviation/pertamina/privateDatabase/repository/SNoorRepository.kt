package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao.SNoorDao
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.CorrFactor
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Density_15
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_20
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_21
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_22
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_11
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.UserPMATH


class SNoorRepository(private val sNoorDao: SNoorDao) {

    fun getResultDensity(density: Double, temprature: Double): LiveData<Density_15?> {
        return sNoorDao.getResultDensity(density, temprature)
    }

    fun getResultTangki9(mm: Double): LiveData<Tangki_9?> {
        return sNoorDao.getResultTangki9(mm)
    }

    fun getResultTangki10(mm: Double): LiveData<Tangki_10?> {
        return sNoorDao.getResultTangki10(mm)
    }

    fun getResultTangki11(mm: Double): LiveData<Tangki_11?> {
        return sNoorDao.getResultTangki11(mm)
    }

    fun getResultSnr20(mm: Double): LiveData<Snr_20?> {
        return sNoorDao.getResultSnr20(mm)
    }

    fun getResultSnr21(mm: Double): LiveData<Snr_21?> {
        return sNoorDao.getResultSnr21(mm)
    }

    fun getResultSnr22(mm: Double): LiveData<Snr_22?> {
        return sNoorDao.getResultSnr22(mm)
    }

    fun getResultCorrFactor(densityReal: String): LiveData<CorrFactor?> {
        return sNoorDao.getResultCorrFactor(densityReal)
    }

    fun getUserPMATH(username: String, password: String): LiveData<Boolean> {
        val result = sNoorDao.getUser(username, password).map { it != null }
        return result
    }

}