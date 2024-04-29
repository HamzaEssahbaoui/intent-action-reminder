package com.intentaction.reminder.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.intentaction.reminder.R
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.helpers.ReminderScheduler
import com.intentaction.reminder.viewmodel.IntentActionViewModel
import java.util.*

class AddIntentActionDialog(private val viewModel: IntentActionViewModel) : DialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_intent, null)

            val preReminderButton = view.findViewById<Button>(R.id.pre_reminder_button)
            preReminderButton.setOnClickListener {
                showDateTimePicker { dateInMillis ->
                    preReminderButton.text = dateInMillis.toString()
                }
            }

            val urgentReminderButton = view.findViewById<Button>(R.id.urgent_reminder_button)
            urgentReminderButton.setOnClickListener {
                showDateTimePicker { dateInMillis ->
                    urgentReminderButton.text = dateInMillis.toString()
                }
            }

            val confirmationReminderButton = view.findViewById<Button>(R.id.confirmation_reminder_button)
            confirmationReminderButton.setOnClickListener {
                showDateTimePicker { dateInMillis ->
                    confirmationReminderButton.text = dateInMillis.toString()
                }
            }

            builder.setView(view)
                .setPositiveButton(R.string.save) { _, _ ->
                    val name = view.findViewById<EditText>(R.id.name).text.toString()
                    val quote = view.findViewById<EditText>(R.id.quote).text.toString()
                    val category = view.findViewById<EditText>(R.id.category).text.toString()
                    val preReminder = preReminderButton.text.toString().toLong()
                    val urgentReminder = urgentReminderButton.text.toString().toLong()
                    val confirmationReminder = confirmationReminderButton.text.toString().toLong()

                    // Create an IntentAction object
                    val intentAction = IntentAction(
                        name = name,
                        quote = quote,
                        category = category,
                        preReminderTime = preReminder,
                        urgentReminderTime = urgentReminder,
                        confirmationReminderTime = confirmationReminder,
                        status = "unfulfilled" // default status
                    )

                    // Setup the ReminderScheduler
                    val reminderScheduler = ReminderScheduler(requireContext())
                    reminderScheduler.scheduleIntents(intentAction)


                    // Pass the IntentAction object to the ViewModel
                    viewModel.addIntent(intentAction)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showDateTimePicker(onDateTimePicked: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        onDateTimePicked(calendar.timeInMillis)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}