package com.intentaction.reminder.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intentaction.reminder.R
import com.intentaction.reminder.ui.adapter.IntentActionAdapter
import com.intentaction.reminder.ui.viewmodel.IntentActionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intentaction.reminder.ui.components.AddIntentActionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val viewModel: IntentActionViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           ActivityCompat.requestPermissions(
               this,
               arrayOf(
                   Manifest.permission.POST_NOTIFICATIONS,
                   Manifest.permission.SCHEDULE_EXACT_ALARM,
                   Manifest.permission.RECEIVE_BOOT_COMPLETED),
               1)
        }

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