package com.intentaction.reminder.di


import android.content.Context
import androidx.room.Room
import com.intentaction.reminder.data.AppDatabase
import com.intentaction.reminder.data.dao.ActionIntentDao
import com.intentaction.reminder.data.repository.IntentRepository
import com.intentaction.reminder.data.repository.IntentRepositoryImpl
import com.intentaction.reminder.data.services.SchedulerService
import com.intentaction.reminder.data.services.SchedulerServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AppModule {

    @Binds
    fun intentRepository(
        repo: IntentRepositoryImpl
    ): IntentRepository

    @Binds
    fun schedulerService(
        serviceImpl: SchedulerServiceImpl
    ): SchedulerService

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(
            @ApplicationContext context: Context
        ): AppDatabase = Room.databaseBuilder(
            context = context.applicationContext,
            klass = AppDatabase::class.java,
            name = "action_intent_database"
        ).fallbackToDestructiveMigration()
            .build()

        @Provides
        fun provideIntentDao(appDatabase: AppDatabase): ActionIntentDao = appDatabase.intentDao()
    }
}

