package com.intentaction.reminder.ui.screens

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.intentaction.reminder.R
import com.intentaction.reminder.ui.viewmodel.IntentActionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class IntentActionDetailsScreen : AppCompatActivity() {
    private val viewModel: IntentActionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_details)

        val intentId = intent.getIntExtra("INTENT_ID", 0)

        // Correctly launch the coroutine in an IO context
        lifecycleScope.launch(Dispatchers.IO) {
            val intentAction = viewModel.getIntentById(intentId)

            withContext(Dispatchers.Main) {
                // UI updates must be done on the main thread
                findViewById<TextView>(R.id.intent_name).text = intentAction?.name
                findViewById<TextView>(R.id.intent_category).text = intentAction?.category
                findViewById<TextView>(R.id.intent_quote).text = intentAction?.quote
            }

            intentAction?.let {
                it.status = "fulfilled"
                viewModel.updateIntent(it)
            }
        }
    }
}
