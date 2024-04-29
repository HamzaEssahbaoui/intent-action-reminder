package com.intentaction.reminder.db

import android.content.Context
import androidx.room.TypeConverters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.helpers.Converters

@Database(entities = [IntentAction::class], version = 1)
@TypeConverters(Converters::class) // Include this if you have custom type converters
abstract class AppDatabase : RoomDatabase() {
    abstract fun intentDao(): ActionIntentDao

    companion object {
        const val DATABASE_VERSION = 8
        const val DATABASE_NAME = "action_intent_database"

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance!!
        }
    }
}


