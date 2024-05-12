package com.intentaction.reminder.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intentaction.reminder.data.entity.IntentAction
import com.intentaction.reminder.data.repository.IntentRepository
import com.intentaction.reminder.data.services.SchedulerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntentActionViewModel @Inject constructor(
    private val intentRepository: IntentRepository,
    private val schedulerService: SchedulerService
) : ViewModel() {

    val TAG: String =
        "IntentActionViewModel" // This is a constant, so it should be declared with val


    // Add a new intentAction to the database
    @RequiresApi(Build.VERSION_CODES.O)
    fun addIntent(intentAction: IntentAction) = viewModelScope.launch {
        try {
            val updatedIntentAction = intentRepository.insertIntent(intentAction)
            schedulerService.scheduleIntents(updatedIntentAction)
            Log.v(
                "ReminderLifeCycle",
                "IntentAction added with ID: ${updatedIntentAction.id} ,name : ${updatedIntentAction.name} , dueDate : ${updatedIntentAction.dueDate} , status : ${updatedIntentAction.status} , category : ${updatedIntentAction.category}"
            )
        } catch (e: Exception) {
            Log.d(TAG, "Error adding intent: ${e.message}")
        }
    }


    // Other methods for updating and deleting intents
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateIntentStatus(intentAction: IntentAction?, newStatus: String) = viewModelScope.launch {
        intentRepository.updateIntentStatus(intentAction, newStatus)
        /*
         cancel reminder when user dismissed
         ***
         if (intentAction?.status == "unfulfilled"){
             DateTimeConverter.fromZonedDateTimeToMillis(intentAction.dueDate)
                 ?.let { schedulerService.cancelAlarm(intentAction , it) }
         } */
    }

    fun dismissIntent(intentAction: IntentAction?) = viewModelScope.launch {
        intentRepository.dismissIntent(intentAction)
    }


    fun updateIntent(intentAction: IntentAction?) = viewModelScope.launch {
        intentAction?.let {
            intentRepository.updateIntent(it)
        } ?: run {
            // Handle the case where intentAction is null, if necessary
        }
    }

    fun deleteIntent(intentAction: IntentAction?) = viewModelScope.launch {
        if (intentAction != null) {
            intentRepository.deleteIntent(intentAction)
        }
    }

    // other functions for getting intents, they return LiveData objects

    val intents: LiveData<List<IntentAction>> = intentRepository.getIntents()

    suspend fun getIntentById(id: Int) = intentRepository.getIntentById(id)


    fun getIntentsByStatus(status: String) = intentRepository.getIntentsByStatus(status)

    fun getIntentsByCategory(category: String) = intentRepository.getIntentsByCategory(category)

    fun getIntentsByStatusAndCategory(status: String, category: String) =
        intentRepository.getIntentsByStatusAndCategory(status, category)


}