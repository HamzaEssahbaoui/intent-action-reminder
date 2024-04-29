package com.intentaction.reminder.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.intentaction.reminder.R
import com.intentaction.reminder.viewmodel.IntentActionViewModel
import kotlinx.coroutines.launch

class IntentActionDetailsScreen : AppCompatActivity() {
    private val viewModel: IntentActionViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_details)

        // Get the intent ID passed from the notification
        val intentId = intent.getIntExtra("INTENT_ID", 0)

        // Fetch the IntentAction object from the database
        lifecycleScope.launch {
            val intent = viewModel.getIntentById(intentId).value

            // Get the TextViews
            val nameTextView = findViewById<TextView>(R.id.intent_name)
            val categoryTextView = findViewById<TextView>(R.id.intent_category)
            val quoteTextView = findViewById<TextView>(R.id.intent_quote)

            // Set the text of the TextViews
            nameTextView.text = intent?.name
            categoryTextView.text = intent?.category
            quoteTextView.text = intent?.quote

            // Update the status of the IntentAction object to "fulfilled"
            intent?.status = "fulfilled"

            viewModel.updateIntent(intent)
        }
    }
}