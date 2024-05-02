package com.intentaction.reminder.repository

import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.helpers.ReminderScheduler
import javax.inject.Inject

class IntentRepository @Inject constructor (
    private val actionIntentDao: ActionIntentDao,
    private val reminderScheduler: ReminderScheduler
)
{


    fun getIntents() = actionIntentDao.getAllIntents()

    suspend fun insertIntent(intentAction: IntentAction) {
        actionIntentDao.insertIntent(intentAction)
    }

    // update the intentAction status
    suspend fun updateIntentStatus(intentAction: IntentAction?, newStatus: String) {
        val updatedIntent = intentAction?.copy(status = newStatus)
        actionIntentDao.updateIntent(updatedIntent!!)
    }

    suspend fun dissmisIntent(intentAction: IntentAction?){
        val updatedIntent = intentAction?.copy(status = "dissmised")
        actionIntentDao.updateIntent(updatedIntent!!)
        reminderScheduler.cancelAlarm(intentAction)
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
    fun getIntentById(id: Int) = actionIntentDao.getIntentById(id)

    // get intent in a specific date range
    fun getIntentsInDateRange(startDate: Long, endDate: Long) = actionIntentDao.getIntentsInDateRange(startDate, endDate)

    // get all intents with a specific status and in a specific date range
    fun getIntentsByStatusInDateRange(status: String, startDate: Long, endDate: Long) = actionIntentDao.getIntentsByStatusInDateRange(status, startDate, endDate)


    // get all the unfulfilled intents


    suspend fun getUnfulfilledIntents(): List<IntentAction> {
        return actionIntentDao.getUnfulfilledIntents()
    }
}
