package com.intentaction.reminder.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.receivers.ReminderBroadcastReceiver

class ReminderScheduler(private val context: Context) {

    fun scheduleIntents(intentAction: IntentAction) {
        scheduleAlarm(intentAction.preReminderTime, intentAction)
        scheduleAlarm(intentAction.urgentReminderTime, intentAction)
        scheduleAlarm(intentAction.confirmationReminderTime, intentAction)
    }

    fun scheduleAlarm(time: Long, intentAction: IntentAction) {
        val alarmIntent = android.content.Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_REMINDER
            putExtra("INTENT_ID", intentAction.id)
            putExtra("INTENT_TITLE", intentAction.name)
            putExtra("INTENT_QUOTE", intentAction.quote)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, intentAction.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun cancelIntents(intentAction: IntentAction) {
        cancelAlarm(intentAction)
        cancelAlarm(intentAction)
        cancelAlarm(intentAction)
    }

    fun cancelAlarm(intentAction: IntentAction) {
        val alarmIntent = android.content.Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, intentAction.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}