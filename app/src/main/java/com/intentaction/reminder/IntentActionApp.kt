package com.intentaction.reminder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IntentActionApp : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}