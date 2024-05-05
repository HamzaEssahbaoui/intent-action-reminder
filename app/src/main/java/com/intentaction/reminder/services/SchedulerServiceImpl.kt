package com.intentaction.reminder.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.intentaction.reminder.db.converters.DateTimeConverter
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.receivers.ReminderBroadcastReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class SchedulerServiceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : SchedulerService {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun scheduleIntents(intentAction: IntentAction) {

        DateTimeConverter.fromZonedDateTimeToMillis(intentAction.dueDate)?.let {
            scheduleIntentAlarm(it, intentAction)
        }
    }


    private fun scheduleIntentAlarm(time: Long, intentAction: IntentAction) {
        val alarmIntent = Intent(
            context,
            ReminderBroadcastReceiver::class.java
        )
            .apply {
                action = ReminderBroadcastReceiver.ACTION_REMINDER
                putExtra("INTENT_ID", intentAction.id)
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            intentAction.id,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }


    override fun cancelAlarm(intentAction: IntentAction?) {
        val alarmIntent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = intentAction?.let {
            PendingIntent.getBroadcast(
                context,
                it.id,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }
}
