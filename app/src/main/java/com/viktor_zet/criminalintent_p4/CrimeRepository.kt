package com.viktor_zet.criminalintent_p4

import android.content.Context
import androidx.room.Room
import com.viktor_zet.criminalintent_p4.database.CrimeDatabse



class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabse = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabse::class.java,
        DATABASE_NAME
    ).build()

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