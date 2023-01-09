package com.viktor_zet.criminalintent_p4.database

import androidx.room.Dao
import androidx.room.Query
import com.viktor_zet.criminalintent_p4.Crime
import java.util.UUID

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
    suspend fun getCrimes(): List<Crime>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun getCrime(id: UUID): Crime
}