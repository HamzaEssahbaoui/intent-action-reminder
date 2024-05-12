package com.intentaction.reminder.ui.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.intentaction.reminder.R
import com.intentaction.reminder.data.converters.DateTimeConverter
import com.intentaction.reminder.data.entity.IntentAction
import com.intentaction.reminder.ui.viewmodel.IntentActionViewModel
import java.time.ZonedDateTime
import java.util.*

class AddIntentActionDialog constructor(
    private val viewModel: IntentActionViewModel
) : DialogFragment() {

    val TAG: String = "AddIntentActionDialog" // This is a constant, so it should be declared with val
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_intent, null)
            var convertedTime: ZonedDateTime? = null
            val dueDateButton = view.findViewById<Button>(R.id.due_date)
            dueDateButton.setOnClickListener {
                showDateTimePicker { dateInMillis ->
                    convertedTime = DateTimeConverter.fromMillisToZoneDateTime(dateInMillis)
                }
            }


            builder.setView(view)
                .setPositiveButton(R.string.save) { _, _ ->
                    val name = view.findViewById<EditText>(R.id.name).text.toString()
                    val quote = view.findViewById<EditText>(R.id.quote).text.toString()
                    val category = view.findViewById<EditText>(R.id.category).text.toString()
                    val dueDate = convertedTime

                    // Create an IntentAction object
                    val intentAction = IntentAction(
                        name = name,
                        quote = quote,
                        category = category,
                        dueDate = dueDate,
                        status = "unfulfilled" // default status
                    )

                    // Pass the IntentAction object to the ViewModel
                    try {
                        viewModel.addIntent(intentAction)

                    } catch (e: Exception) {
                        Log.d(TAG, "Error adding intent: ${e.message}")
                    }
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