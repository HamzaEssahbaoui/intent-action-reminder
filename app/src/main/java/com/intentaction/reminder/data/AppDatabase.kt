package com.intentaction.reminder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.intentaction.reminder.data.converters.Converters
import com.intentaction.reminder.data.converters.DateTimeConverter
import com.intentaction.reminder.data.dao.ActionIntentDao
import com.intentaction.reminder.data.entity.IntentAction

@Database(
    entities = [IntentAction::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class) // Include this if you have custom type converters
abstract class AppDatabase : RoomDatabase() {
    abstract fun intentDao(): ActionIntentDao
}


