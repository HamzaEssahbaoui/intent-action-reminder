package com.intentaction.reminder.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "intent_actions" )
data class IntentAction(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String? = null,
    val quote: String? = null,
    val category: String? = null,
    val dueDate: ZonedDateTime? = null,
    var status: String? // "fulfilled" or "unfulfilled"
)
