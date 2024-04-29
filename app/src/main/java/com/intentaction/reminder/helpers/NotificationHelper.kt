package com.intentaction.reminder.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.intentaction.reminder.ui.screens.IntentActionDetailsScreen

object NotificationHelper {

    private const val CHANNEL_ID = "REMINDER_CHANNEL_ID"
    private const val CHANNEL_NAME = "Reminder Notifications"
    private const val CHANNEL_DESCRIPTION = "Notifications for reminders"

    fun createNotification(context: Context, intentAction: com.intentaction.reminder.db.entity.IntentAction, icon: Int) {
        createNotificationChannel(context)

        val notificationIntent = Intent(context, IntentActionDetailsScreen::class.java).apply {
            putExtra("INTENT_ID", intentAction.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(intentAction.name)
            .setContentText(intentAction.quote)
            .setSmallIcon(icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(intentAction.id, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}