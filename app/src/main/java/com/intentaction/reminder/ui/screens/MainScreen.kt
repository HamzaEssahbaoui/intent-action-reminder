package com.intentaction.reminder.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intentaction.reminder.R
import com.intentaction.reminder.adapter.IntentActionAdapter
import com.intentaction.reminder.viewmodel.IntentActionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val viewModel: IntentActionViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = IntentActionAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.intents.observe(this) { intents ->
            // Update the cached copy of the intents in the adapter.
            intents?.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val dialog = AddIntentActionDialog(viewModel)
            dialog.show(supportFragmentManager, "AddIntentActionDialog")
        }
    }
}