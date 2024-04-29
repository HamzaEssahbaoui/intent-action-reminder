package com.intentaction.reminder.ui.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.intentaction.reminder.R

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000 // 3 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this, MainScreen::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}