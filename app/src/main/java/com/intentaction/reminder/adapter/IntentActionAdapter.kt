package com.intentaction.reminder.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intentaction.reminder.R
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.viewmodel.IntentActionViewModel

class IntentActionAdapter(private val viewModel: IntentActionViewModel) :
    ListAdapter<IntentAction, IntentActionAdapter.IntentViewHolder>(IntentsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntentViewHolder {
        return IntentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IntentViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class IntentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val intentNameView: TextView = view.findViewById(R.id.intent_name)
        private val intentQuoteView: TextView = view.findViewById(R.id.intent_quote)

        fun bind(intentAction: IntentAction?) {
            intentNameView.text = intentAction?.name
            intentQuoteView.text = intentAction?.quote
        }

        companion object {
            fun create(parent: ViewGroup): IntentViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.intent_item, parent, false)
                return IntentViewHolder(view)
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