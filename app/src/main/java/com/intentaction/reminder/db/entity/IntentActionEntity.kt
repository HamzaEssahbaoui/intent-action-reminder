package com.intentaction.reminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intents")
data class IntentAction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quote: String,
    val category: String,
    val preReminderTime: Long,
    val urgentReminderTime: Long,
    val confirmationReminderTime: Long,
    var status: String // "fulfilled" or "unfulfilled"
)
