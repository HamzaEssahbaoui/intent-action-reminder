package com.intentaction.reminder.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.repository.IntentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntentActionViewModel @Inject constructor(
    private val intentRepository: IntentRepository

) : ViewModel() {

    val TAG: String = "IntentActionViewModel" // This is a constant, so it should be declared with val


    // suspended functions are used to perform long-running tasks, they return a result


    fun addIntent(intentAction: IntentAction) = viewModelScope.launch {

        try {
            intentRepository.insertIntent(intentAction)
        } catch (e: Exception) {
            Log.d(TAG, "Error inserting intent: ${e.message}")
        }
    }

    // Other methods for updating and deleting intents
    fun updateIntentStatus(intentAction: IntentAction, newStatus: String) = viewModelScope.launch {
        intentRepository.updateIntentStatus(intentAction, newStatus)
    }

    fun updateIntent(intentAction: IntentAction?) = viewModelScope.launch {
        intentAction?.let {
            intentRepository.updateIntent(it)
        } ?: run {
            // Handle the case where intentAction is null, if necessary
        }
    }

    fun deleteIntent(intentAction: IntentAction) = viewModelScope.launch {
        intentRepository.deleteIntent(intentAction)
    }

    // other functions for getting intents, they return LiveData objects

    val intents: LiveData<List<IntentAction>> = intentRepository.getIntents()

    fun getIntentById(id: Int) = intentRepository.getIntentById(id)


    fun getIntentsByStatus(status: String) = intentRepository.getIntentsByStatus(status)

    fun getIntentsByCategory(category: String) = intentRepository.getIntentsByCategory(category)

    fun getIntentsByStatusAndCategory(status: String, category: String) = intentRepository.getIntentsByStatusAndCategory(status, category)




}