package com.intentaction.reminder.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.intentaction.reminder.R
import com.intentaction.reminder.helpers.NotificationHelper
import com.intentaction.reminder.helpers.ReminderScheduler
import com.intentaction.reminder.repository.IntentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderBroadcastReceiver : BroadcastReceiver() {
    val TAG = "ReminderBroadcastReceiver"
        @Inject
        lateinit var intentRepository: IntentRepository

        @Inject
        lateinit var reminderScheduler: ReminderScheduler
    companion object {
        const val ACTION_REMINDER = "com.intentaction.reminder.ACTION_REMINDER"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_REMINDER -> {
                val intentId = intent.getIntExtra("INTENT_ID", 0)
                CoroutineScope(Dispatchers.IO).launch {
                    val reminderIntentLiveData = intentRepository.getIntentById(intentId)
                    val reminderIntent = reminderIntentLiveData.value
                    reminderIntent?.let {
                        NotificationHelper.createNotification(context, it, R.drawable.ic_launcher_foreground)
                        Log.d(TAG, "Reminder notification created for ${it.name}")
                    }
                }
            }

            ACTION_BOOT_COMPLETED -> {
                rescheduleAlarms()
            }

            else -> Log.d("ReminderBroadcastReceiver", "Unknown intent received")
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun rescheduleAlarms() {
        CoroutineScope(Dispatchers.IO).launch {
            val intents = intentRepository.getUnfulfilledIntents()
            intents.forEach { reminderScheduler.scheduleIntents(it) }
        }
    }
}

