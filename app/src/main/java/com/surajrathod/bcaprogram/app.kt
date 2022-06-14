package com.surajrathod.bcaprogram

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class app : Application() {
    override fun onCreate() {

        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}