package com.viktor_zet.criminalintent_p4.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.viktor_zet.criminalintent_p4.Crime

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabse : RoomDatabase() {

    abstract fun crimeDao(): CrimeDao
}