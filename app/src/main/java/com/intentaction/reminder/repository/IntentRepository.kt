package com.intentaction.reminder.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.services.SchedulerService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IntentRepository @Inject constructor (
    @ApplicationContext
    private val context: Context,
    private val actionIntentDao: ActionIntentDao,
    private val schedulerService: SchedulerService
)
{


    fun getIntents() = actionIntentDao.getAllIntents()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertIntent(intentAction: IntentAction): IntentAction {
        val generatedId = actionIntentDao.insertIntent(intentAction)
        intentAction.id = generatedId.toInt()
        return intentAction
    }

    // update the intentAction status
    suspend fun updateIntentStatus(intentAction: IntentAction?, newStatus: String) {
        val updatedIntent = intentAction?.copy(status = newStatus)
        actionIntentDao.updateIntent(updatedIntent!!)
    }

    suspend fun dismissIntent(intentAction: IntentAction?): Result<Unit> {
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




    suspend fun updateIntent(intentAction: IntentAction) {
        actionIntentDao.updateIntent(intentAction)
    }
    // delete intentAction
    suspend fun deleteIntent(intentAction: IntentAction) {
        actionIntentDao.deleteIntent(intentAction)
    }

    // get all intents with a specific status
    fun getIntentsByStatus(status: String) = actionIntentDao.getIntentsByStatus(status)

    // get all intents with a specific category
    fun getIntentsByCategory(category: String) = actionIntentDao.getIntentsByCategory(category)

    // get all intents with a specific status and category
    fun getIntentsByStatusAndCategory(status: String, category: String) = actionIntentDao.getIntentsByStatusAndCategory(status, category)

    // get intent by id
    suspend fun getIntentById(id: Int) = actionIntentDao.getIntentById(id)

    // get intent in a specific date range
    fun getIntentsInDateRange(startDate: Long, endDate: Long) = actionIntentDao.getIntentsInDateRange(startDate, endDate)

    // get all intents with a specific status and in a specific date range
    fun getIntentsByStatusInDateRange(status: String, startDate: Long, endDate: Long) = actionIntentDao.getIntentsByStatusInDateRange(status, startDate, endDate)


    // get all the unfulfilled intents


    suspend fun getUnfulfilledIntents(): List<IntentAction> {
        return actionIntentDao.getUnfulfilledIntents()
    }
}
