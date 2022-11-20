package com.surajrathod.bcaprogram

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.onesignal.OneSignal
import com.surajrathod.bcaprogram.utils.Constants.ONESIGNAL_APP_ID

class app : Application() {
    override fun onCreate() {

        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}