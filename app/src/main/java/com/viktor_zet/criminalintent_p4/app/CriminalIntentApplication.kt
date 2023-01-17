package com.viktor_zet.criminalintent_p4.app

import android.app.Application
import com.viktor_zet.criminalintent_p4.repository.CrimeRepository

class CriminalIntentApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}