package com.intentaction.reminder.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.services.SchedulerService
import javax.inject.Inject

internal class IntentRepositoryImpl @Inject constructor(
    private val actionIntentDao: ActionIntentDao,
    private val schedulerService: SchedulerService
) : IntentRepository {
    override fun getIntents(): LiveData<List<IntentAction>> = actionIntentDao.getAllIntents()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun insertIntent(intentAction: IntentAction): IntentAction {
        val generatedId = actionIntentDao.insertIntent(intentAction)
        intentAction.id = generatedId.toInt()
        return intentAction
    }

    // update the intentAction status
    override suspend fun updateIntentStatus(intentAction: IntentAction?, newStatus: String) {
        val updatedIntent = intentAction?.copy(status = newStatus)
        actionIntentDao.updateIntent(updatedIntent!!)
    }

    override suspend fun dismissIntent(intentAction: IntentAction?): Result<Unit> {
        return try {
            intentAction?.let {
                val updatedIntent = it.copy(status = "dismissed")
                actionIntentDao.updateIntent(updatedIntent)
                schedulerService.cancelAlarm(it)
                Result.success(Unit) // Indicate success
            } ?: Result.failure(Exception("IntentAction was null"))
        } catch (e: Exception) {
            // Handle any exceptions that occur during database access or alarm cancellation
            Result.failure(e)
        }
    }


    override suspend fun updateIntent(intentAction: IntentAction) {
        actionIntentDao.updateIntent(intentAction)
    }

    // delete intentAction
    override suspend fun deleteIntent(intentAction: IntentAction) {
        actionIntentDao.deleteIntent(intentAction)
    }

    // get all intents with a specific status
    override fun getIntentsByStatus(status: String): LiveData<List<IntentAction>> =
        actionIntentDao.getIntentsByStatus(status)

    // get all intents with a specific category
    override fun getIntentsByCategory(category: String): LiveData<List<IntentAction>> =
        actionIntentDao.getIntentsByCategory(category)

    // get all intents with a specific status and category
    override fun getIntentsByStatusAndCategory(
        status: String,
        category: String
    ): LiveData<List<IntentAction>> =
        actionIntentDao.getIntentsByStatusAndCategory(status, category)

    // get intent by id
    override suspend fun getIntentById(id: Int): IntentAction? = actionIntentDao.getIntentById(id)

    // get intent in a specific date range
    override fun getIntentsInDateRange(
        startDate: Long,
        endDate: Long
    ): LiveData<List<IntentAction>> =
        actionIntentDao.getIntentsInDateRange(startDate, endDate)

    // get all intents with a specific status and in a specific date range
    override fun getIntentsByStatusInDateRange(
        status: String,
        startDate: Long,
        endDate: Long
    ): LiveData<List<IntentAction>> =
        actionIntentDao.getIntentsByStatusInDateRange(status, startDate, endDate)


    // get all the unfulfilled intents


    override suspend fun getUnfulfilledIntents(): List<IntentAction> {
        return actionIntentDao.getUnfulfilledIntents()
    }
}
