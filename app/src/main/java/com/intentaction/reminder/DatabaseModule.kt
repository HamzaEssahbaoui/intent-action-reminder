package com.intentaction.reminder


import android.content.Context
import androidx.room.Room
import com.intentaction.reminder.db.AppDatabase
import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.repository.IntentRepository
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
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideIntentDao(appDatabase: AppDatabase): ActionIntentDao {
        return appDatabase.intentDao()
    }

    @Provides
    @Singleton
    fun provideIntentRepository(actionIntentDao: ActionIntentDao): IntentRepository {
        return IntentRepository(actionIntentDao)
    }
}

