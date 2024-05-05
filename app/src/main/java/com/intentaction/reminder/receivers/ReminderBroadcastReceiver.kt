package com.intentaction.reminder.receivers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.intentaction.reminder.R
import com.intentaction.reminder.repository.IntentRepository
import com.intentaction.reminder.services.NotificationService
import com.intentaction.reminder.services.SchedulerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderBroadcastReceiver : HiltBroadcastReceiver() {
    private val TAG = "ReminderBroadcastReceiver"

    @Inject
    lateinit var intentRepository: IntentRepository

    @Inject
    lateinit var schedulerService: SchedulerService

    companion object {
        const val ACTION_REMINDER = "com.intentaction.reminder.ACTION_REMINDER"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_REMINDER -> {

                Log.v(TAG, "Reminder received")

                try {
                    scheduleAlarms(intent, context)
                    Log.d(TAG, "Reminder scheduled")
                } catch (e: Exception) {
                    Log.d(TAG, "Error scheduling alarms", e)
                }
            }

            ACTION_BOOT_COMPLETED -> {
                rescheduleAlarms()
            }

            else -> Log.d(TAG, "Unknown intent received")
        }
    }

    private fun scheduleAlarms(intent: Intent, context: Context) {
        val intentId = intent.getIntExtra("INTENT_ID", -1)
        if (intentId == -1) {
            Log.d(TAG, "Intent ID not found")
            return
        }
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            val reminderIntent = intentRepository.getIntentById(intentId)

            if (reminderIntent != null) {
                Log.d(TAG, "Reminder Intent found: $reminderIntent")
                NotificationService.createNotification(
                    context,
                    reminderIntent,
                    R.drawable.ic_launcher_foreground
                )
            } else {
                Log.d(TAG, "Reminder Intent is null for ID: $intentId")
            }

            job.complete()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun rescheduleAlarms() {
        CoroutineScope(Dispatchers.IO).launch {
            val intents = intentRepository.getUnfulfilledIntents()
            intents.forEach { schedulerService.scheduleIntents(it) }
        }
    }
}

