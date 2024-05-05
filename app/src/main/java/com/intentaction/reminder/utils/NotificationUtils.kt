package com.intentaction.reminder.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.ui.screens.IntentActionDetailsScreen

object NotificationUtils {

    private const val CHANNEL_ID = "REMINDER_CHANNEL_ID"
    private const val CHANNEL_NAME = "Reminder Notifications"
    private const val CHANNEL_DESCRIPTION = "Notifications for reminders"
    private val TAG = "NotificationService"
    fun createNotification(context: Context, intentAction: IntentAction?, icon: Int) {
        Log.d(TAG, "Attempting to create notification channel.")
        createNotificationChannel(context)

        val notificationIntent =
            Intent(context, IntentActionDetailsScreen::class.java)
                .apply {
                    putExtra("INTENT_ID", intentAction?.id)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

        val pendingIntent = PendingIntent.getActivity(
            context,
            intentAction!!.id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(intentAction.name)
            .setContentText(intentAction.quote)
            .setSmallIcon(icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(intentAction.quote))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            Log.d(TAG, "Attempting to post notification for ID: ${intentAction.id}")
            notificationManager.notify(intentAction.id, notification)
            Log.d(TAG, "Notification posted")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to post notification", e)
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                importance
            )
                .apply {
                    description = CHANNEL_DESCRIPTION
                }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created")
        }
    }
}