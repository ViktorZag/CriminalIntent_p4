package com.viktor_zet.criminalintent_p4

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CrimeDetailFragmentTest {
    private lateinit var fragmentScenario: FragmentScenario<CrimeDetailFragment>

    @Before
    fun setUp() {
        fragmentScenario = launchFragmentInContainer()
        fragmentScenario.moveToState(Lifecycle.State.RESUMED)
    }

    @After
    fun tearDown() {
        fragmentScenario.close()
    }

    @Test
    fun updatesCrimeAfterTextChange() {
        onView(withId(R.id.crime_title)).perform(replaceText("expected text"))
        fragmentScenario.onFragment { fragment ->
            assertEquals("expected text", fragment.crime.title)
        }
    }
}