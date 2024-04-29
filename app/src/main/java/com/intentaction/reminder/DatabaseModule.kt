package com.intentaction.reminder

import AppDatabase
import android.content.Context
import androidx.room.Room
import com.intentaction.reminder.db.dao.ActionIntentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "action_intent_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideIntentDao(appDatabase: AppDatabase): ActionIntentDao {
        return appDatabase.intentDao()
    }
}
