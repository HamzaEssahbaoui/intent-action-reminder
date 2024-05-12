package com.intentaction.reminder.data.services

import com.intentaction.reminder.data.entity.IntentAction

interface SchedulerService {

    fun scheduleIntents(intentAction: IntentAction)

    fun cancelAlarm(intentAction: IntentAction?)

}