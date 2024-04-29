package com.intentaction.reminder.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.intentaction.reminder.DatabaseModule
import com.intentaction.reminder.R
import com.intentaction.reminder.helpers.NotificationHelper
import com.intentaction.reminder.helpers.ReminderScheduler
import com.intentaction.reminder.repository.IntentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderBroadcastReceiver : BroadcastReceiver() {

        @Inject
        lateinit var intentRepository: IntentRepository

        @Inject
        lateinit var reminderScheduler: ReminderScheduler
    companion object {
        const val ACTION_REMINDER = "com.intent.reminder.ACTION_REMINDER"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_REMINDER -> {
                val intentId = intent.getIntExtra("INTENT_ID", 0)
                CoroutineScope(Dispatchers.IO).launch {
                    val reminderIntentLiveData = intentRepository.getIntentById(intentId)
                    val reminderIntent = reminderIntentLiveData.value
                    reminderIntent?.let {
                        NotificationHelper.createNotification(context, it, R.drawable.ic_launcher_foreground)
                    }
                }
            }

            ACTION_BOOT_COMPLETED -> {
                rescheduleAlarms()
            }

            else -> Log.d("ReminderBroadcastReceiver", "Unknown intent received")
        }
    }



    private fun rescheduleAlarms() {
        CoroutineScope(Dispatchers.IO).launch {
            val intents = intentRepository.getUnfulfilledIntents()
            intents.forEach { reminderScheduler.scheduleIntents(it) }
        }
    }
}

