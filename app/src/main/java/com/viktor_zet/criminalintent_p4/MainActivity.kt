package com.viktor_zet.criminalintent_p4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.format.DateFormat
import android.util.Log
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dateFormat = DateFormat.format("EEEE/MM/dd/yyyy", Date())
        Log.d(
            "date",
            dateFormat.replaceFirst(
                "\\w".toRegex(),
                dateFormat[0].uppercase()
            )
        )
    }
}