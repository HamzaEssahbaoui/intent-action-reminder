package com.intentaction.reminder.data.repository

import androidx.lifecycle.LiveData
import com.intentaction.reminder.data.entity.IntentAction

interface IntentRepository {

    fun getIntents(): LiveData<List<IntentAction>>

    suspend fun insertIntent(intentAction: IntentAction): IntentAction

    // update the intentAction status
    suspend fun updateIntentStatus(intentAction: IntentAction?, newStatus: String)

    suspend fun dismissIntent(intentAction: IntentAction?): Result<Unit>

    suspend fun updateIntent(intentAction: IntentAction)

    // delete intentAction
    suspend fun deleteIntent(intentAction: IntentAction)

    // get all intents with a specific status
    fun getIntentsByStatus(status: String): LiveData<List<IntentAction>>

    // get all intents with a specific category
    fun getIntentsByCategory(category: String): LiveData<List<IntentAction>>

    // get all intents with a specific status and category
    fun getIntentsByStatusAndCategory(
        status: String,
        category: String
    ): LiveData<List<IntentAction>>

    // get intent by id
    suspend fun getIntentById(id: Int): IntentAction?

    // get intent in a specific date range
    fun getIntentsInDateRange(startDate: Long, endDate: Long): LiveData<List<IntentAction>>

    // get all intents with a specific status and in a specific date range
    fun getIntentsByStatusInDateRange(
        status: String,
        startDate: Long,
        endDate: Long
    ): LiveData<List<IntentAction>>


    // get all the unfulfilled intents

    suspend fun getUnfulfilledIntents(): List<IntentAction>

}