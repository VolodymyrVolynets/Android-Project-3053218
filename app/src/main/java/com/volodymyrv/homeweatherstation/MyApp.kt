package com.volodymyrv.homeweatherstation

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Room Database
        AppDatabase.getDatabase(this)
    }
}