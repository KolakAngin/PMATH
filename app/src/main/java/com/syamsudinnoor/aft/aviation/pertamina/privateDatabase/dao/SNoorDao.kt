package com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.CorrFactor
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Density_15
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_20
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_21
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Snr_22
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_10
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_11
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.Tangki_9
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.UserPMATH

@Dao
interface SNoorDao {

    // Mengembalikan LiveData yang bisa bernilai null jika data tidak ditemukan
    @Query("SELECT * FROM density_15 WHERE density_obsd = :density AND temprature = :temprature LIMIT 1")
    fun getResultDensity(density: Double, temprature: Double): LiveData<Density_15?>

    @Query("SELECT * FROM tangki_9 WHERE mm = :mm LIMIT 1")
    fun getResultTangki9(mm: Double): LiveData<Tangki_9?>

    @Query("SELECT * FROM tangki_10 WHERE mm = :mm LIMIT 1")
    fun getResultTangki10(mm: Double): LiveData<Tangki_10?>

    @Query("SELECT * FROM tangki_11 WHERE mm = :mm LIMIT 1")
    fun getResultTangki11(mm: Double): LiveData<Tangki_11?>

    @Query("SELECT * FROM snr_20 WHERE mm = :mm LIMIT 1")
    fun getResultSnr20(mm: Double): LiveData<Snr_20?>

    @Query("SELECT * FROM snr_21 WHERE mm = :mm LIMIT 1")
    fun getResultSnr21(mm: Double): LiveData<Snr_21?>

    @Query("SELECT * FROM snr_22 WHERE mm = :mm LIMIT 1")
    fun getResultSnr22(mm: Double): LiveData<Snr_22?>

    @Query("SELECT * FROM corr_factor WHERE density_real = :densityReal LIMIT 1")
    fun getResultCorrFactor(densityReal: String): LiveData<CorrFactor?>


    @Query("SELECT * FROM user WHERE nama = :username AND password = :password LIMIT 1")
    fun getUser(username: String, password: String): LiveData<UserPMATH?>

    @Query("SELECT * from user where nama = :username")
    fun getUserOnlineName(username: String): LiveData<UserPMATH?>

    @Query("SELECT * from user where password = :password")
    fun getUserOnlyfromPassword(password: String) : LiveData<UserPMATH?>

    @Query("SELECT * FROM user")
    fun getAllUser(): LiveData<List<UserPMATH>>


}