package com.volodymyrv.homeweatherstation

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE)

    fun saveSettings(isSaveTemperature: Boolean, isSaveHumidity: Boolean, isSavePressure: Boolean, isSaveLuminosity: Boolean, howOftenSave: Float) {
        preferences.edit().apply {
            putBoolean("IS_SAVE_TEMPERATURE", isSaveTemperature)
            putBoolean("IS_SAVE_HUMIDITY", isSaveHumidity)
            putBoolean("IS_SAVE_PRESSURE", isSavePressure)
            putBoolean("IS_SAVE_LUMINOSITY", isSaveLuminosity)
            putFloat("HOW_OFTEN_SAVE", howOftenSave)
            apply()
        }
    }

    fun loadSettings(): Settings {
        return Settings(
            preferences.getBoolean("IS_SAVE_TEMPERATURE", true),
            preferences.getBoolean("IS_SAVE_HUMIDITY", true),
            preferences.getBoolean("IS_SAVE_PRESSURE", true),
            preferences.getBoolean("IS_SAVE_LUMINOSITY", true),
            preferences.getFloat("HOW_OFTEN_SAVE", 30f)
        )
    }

    data class Settings(
        val isSaveTemperature: Boolean,
        val isSaveHumidity: Boolean,
        val isSavePressure: Boolean,
        val isSaveLuminosity: Boolean,
        val howOftenSave: Float
    )
}
