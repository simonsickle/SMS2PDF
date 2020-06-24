package com.simonsickle.sms2pdf.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simonsickle.sms2pdf.databinding.ConversationRecyclerRowBinding
import com.simonsickle.sms2pdf.models.Conversations

class ConversationViewHolder(val binding: ConversationRecyclerRowBinding) :
    RecyclerView.ViewHolder(binding.root)

class ConversationAdapter(private val onClick: (Int) -> Unit) : RecyclerView.Adapter<ConversationViewHolder>() {
    var conversations = listOf<Conversations>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConversationRecyclerRowBinding.inflate(inflater, parent, false)
        return ConversationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.binding.txtConversationName.text =
            conversations[holder.adapterPosition].address
        holder.binding.root.setOnClickListener {
            onClick(conversations[holder.adapterPosition].threadID)
        }
    }
}