package com.viktor_zet.criminalintent_p4

import android.app.Application

class CriminalIntentApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}