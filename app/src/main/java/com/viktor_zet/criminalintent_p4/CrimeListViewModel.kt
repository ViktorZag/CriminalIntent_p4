package com.viktor_zet.criminalintent_p4

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    private val repository = CrimeRepository.get()
    val crimes = repository.getCrimes()

    init {
        viewModelScope.launch {

        }
    }


}