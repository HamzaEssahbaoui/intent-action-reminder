package com.intentaction.reminder.adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.intentaction.reminder.R
import com.intentaction.reminder.db.converters.DateTimeConverter
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.viewmodel.IntentActionViewModel

class IntentActionAdapter(private val viewModel: IntentActionViewModel) :
    ListAdapter<IntentAction, IntentActionAdapter.IntentViewHolder>(IntentsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntentViewHolder {
        return IntentViewHolder.create(parent, viewModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: IntentViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class IntentViewHolder(private val view: View , private val viewModel: IntentActionViewModel) : RecyclerView.ViewHolder(view) {
        private val intentNameView: TextView = view.findViewById(R.id.intentName)
        private val intentQuoteView: TextView = view.findViewById(R.id.intentQuote)
        private val intentDueDateView: TextView = view.findViewById(R.id.dueDate)
        private val intentCategoryView: TextView = view.findViewById(R.id.intentCategory)
        private val intentStatusView: TextView = view.findViewById(R.id.intentStatus)
        private val editButton: MaterialButton = view.findViewById(R.id.editButton)
        private val dismissButton: MaterialButton = view.findViewById(R.id.dismissButton)
        private val deleteButton: MaterialButton = view.findViewById(R.id.deleteButton)
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(intentAction: IntentAction?) {
            intentNameView.text = intentAction?.name
            intentStatusView.text = intentAction?.status
            intentQuoteView.text = intentAction?.quote
            intentDueDateView.text = DateTimeConverter.fromZonedDateTime(intentAction?.dueDate) // This should be converted to a string
            intentCategoryView.text = intentAction?.category
            editButton.setOnClickListener {
                viewModel.updateIntent(intentAction)
            }

            dismissButton.setOnClickListener {
                viewModel.updateIntentStatus(intentAction, "fulfilled")
            }

            deleteButton.setOnClickListener {
                viewModel.deleteIntent(intentAction)
            }
        }

        companion object {
            fun create(parent: ViewGroup, viewModel: IntentActionViewModel): IntentViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.intent_item, parent, false)
                return IntentViewHolder(view, viewModel)
            }
        }
    }

    class IntentsComparator : DiffUtil.ItemCallback<IntentAction>() {
        override fun areItemsTheSame(oldItem: IntentAction, newItem: IntentAction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: IntentAction, newItem: IntentAction): Boolean {
            return oldItem.id == newItem.id
        }
    }
}