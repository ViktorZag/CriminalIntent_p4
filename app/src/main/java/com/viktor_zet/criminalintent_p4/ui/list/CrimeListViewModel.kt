package com.viktor_zet.criminalintent_p4.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viktor_zet.criminalintent_p4.entity.Crime
import com.viktor_zet.criminalintent_p4.repository.CrimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    private val repository = CrimeRepository.get()
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }


}