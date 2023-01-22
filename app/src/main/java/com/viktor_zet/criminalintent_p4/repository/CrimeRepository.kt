package com.viktor_zet.criminalintent_p4.repository

import android.content.Context
import androidx.room.Room
import com.viktor_zet.criminalintent_p4.database.CrimeDatabase
import com.viktor_zet.criminalintent_p4.entity.Crime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID


class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)
    fun updateCrime(crime: Crime) {
        coroutineScope.launch { database.crimeDao().updateCrime(crime) }
    }
    suspend fun addCrime(crime: Crime){
        database.crimeDao().addCrime(crime)
    }
    suspend fun deleteCrime(crime: Crime){
        database.crimeDao().deleteCrime(crime)
    }


    companion object {
        private const val DATABASE_NAME = "crime-database"
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}