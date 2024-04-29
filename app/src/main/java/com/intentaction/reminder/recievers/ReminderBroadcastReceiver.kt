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

class ReminderBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_REMINDER = "com.intent.reminder.ACTION_REMINDER"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_REMINDER -> {
                val intentId = intent.getIntExtra("INTENT_ID", 0)
                CoroutineScope(Dispatchers.IO).launch {
                    val intentDao = DatabaseModule.provideAppDatabase(context).intentDao()
                    val intentRepository = IntentRepository(intentDao)
                    val reminderIntentLiveData = intentRepository.getIntentById(intentId)
                    val reminderIntent = reminderIntentLiveData.value
                    reminderIntent?.let {
                        NotificationHelper.createNotification(context, it, R.drawable.ic_launcher_foreground)
                    }
                }
            }

            ACTION_BOOT_COMPLETED -> {
                rescheduleAlarms(context)
            }

            else -> Log.d("ReminderBroadcastReceiver", "Unknown intent received")
        }
    }

    private fun rescheduleAlarms(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // Get an instance of your database and then get the ActionIntentDao
            val intentDao = DatabaseModule.provideAppDatabase(context).intentDao()
            val intentRepository = IntentRepository(intentDao)
            val intents = intentRepository.getUnfulfilledIntents()
            val reminderScheduler = ReminderScheduler(context)
            intents.forEach { reminderScheduler.scheduleIntents(it) }
        }
    }
}

