package com.viktor_zet.criminalintent_p4.database

import androidx.room.TypeConverter
import java.util.Date

class CrimeTypeConverters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}