package com.intentaction.reminder.services

import com.intentaction.reminder.db.entity.IntentAction

interface SchedulerService {

    fun scheduleIntents(intentAction: IntentAction)

    fun cancelAlarm(intentAction: IntentAction?)

}