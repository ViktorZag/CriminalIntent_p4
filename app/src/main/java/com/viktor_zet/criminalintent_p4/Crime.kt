package com.viktor_zet.criminalintent_p4

import java.util.Date
import java.util.UUID

data class Crime(
    val id: UUID,
    var title: String,
    var date: Date,
    var isSolved: Boolean
)