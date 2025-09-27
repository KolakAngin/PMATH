package com.syamsudinnoor.aft.aviation.pertamina

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Simulasi testing menggunakan AndroidX Test
 *
 * Testing dilakukan untuk menemukan malfungsi sistem dalam pengoperasian logika program
 */
@RunWith(AndroidJUnit4::class)
class TestingStart {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.syamsudinnoor.aft.aviation.pertamina", appContext.packageName)
    }
}