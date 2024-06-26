package com.example.timezoneaware

import android.app.Application
import com.example.timezoneaware.utils.Prefs
import dagger.hilt.android.HiltAndroidApp

val prefs: Prefs by lazy {
    App.prefs!!
}

@HiltAndroidApp
class App(): Application() {
    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}
