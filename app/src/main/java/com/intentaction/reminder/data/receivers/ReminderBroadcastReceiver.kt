package com.intentaction.reminder.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.intentaction.reminder.R
import com.intentaction.reminder.data.repository.IntentRepository
import com.intentaction.reminder.data.services.SchedulerService
import com.intentaction.reminder.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "ReminderBroadcastReceiver"


    companion object {
        const val ACTION_REMINDER = "com.intentaction.reminder.ACTION_REMINDER"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    @Inject
    lateinit var intentRepository: IntentRepository

    @Inject
    lateinit var schedulerService: SchedulerService
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_REMINDER -> {

                Log.v(TAG, "Reminder Intent received")

                try {
                    serveReminder(intent, context)
                    Log.d(TAG, "Reminder is being served")
                } catch (e: Exception) {
                    Log.d(TAG, "Error scheduling reminders", e)
                }
            }

            ACTION_BOOT_COMPLETED -> {
                rescheduleAlarms()
            }

            else -> Log.d(TAG, "Unknown intent received")
        }
    }

    private fun serveReminder(intent: Intent, context: Context) {
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
                NotificationUtils.createNotification(
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

