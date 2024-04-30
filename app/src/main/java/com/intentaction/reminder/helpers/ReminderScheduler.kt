package com.intentaction.reminder.helpers
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.intentaction.reminder.db.converters.DateTimeConverter
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.receivers.ReminderBroadcastReceiver

class ReminderScheduler(private val context: Context) {
    val TAG = "ReminderScheduler"
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleIntents(intentAction: IntentAction) {

        DateTimeConverter.fromZonedDateTimeToMillis(intentAction.dueDate)?.let {
            scheduleIntentAlarm(it, intentAction)
            Log.d(TAG, "Intent scheduled for ${intentAction.dueDate} converted format is $it" )
        }
    }



    private fun scheduleIntentAlarm(time: Long, intentAction: IntentAction) {
        val alarmIntent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_REMINDER
            putExtra("INTENT_ID", intentAction.id)
            putExtra("INTENT_TITLE", intentAction.name)
            putExtra("INTENT_QUOTE", intentAction.quote)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, intentAction.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }


    private fun cancelAlarm(intentAction: IntentAction, time: Long) {
        val alarmIntent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, intentAction.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
