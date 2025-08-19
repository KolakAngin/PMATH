package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.repository

import androidx.lifecycle.LiveData
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao.SNoorDao
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Density_15
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_20
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_21
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_22
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_11
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9


class SNoorRepository(private val sNoorDao: SNoorDao) {

    // Fungsi-fungsi ini hanya meneruskan panggilan ke DAO.
    // Ini membuat kode lebih bersih dan mudah dites.

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
}