package com.intentaction.reminder


import android.content.Context
import androidx.room.Room
import com.intentaction.reminder.db.AppDatabase
import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.repository.IntentRepository
import com.intentaction.reminder.repository.IntentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DatabaseModule {

    @Binds
    fun intentRepository(
        repo: IntentRepositoryImpl
    ): IntentRepository

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

