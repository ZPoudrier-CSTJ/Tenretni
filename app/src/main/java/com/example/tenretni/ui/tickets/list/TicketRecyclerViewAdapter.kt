package com.example.tenretni.ui.tickets.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.ItemTicketBinding
import com.example.tenretni.domain.models.Ticket

class TicketRecyclerViewAdapter (
    var tickets: List<Ticket> = listOf(),
    private val onTicketClick: (Ticket) -> Unit): RecyclerView.Adapter<TicketRecyclerViewAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.bind(ticket)

        holder.itemView.setOnClickListener{
            onTicketClick(ticket)
        }


    }
    override fun getItemCount() = tickets.size
    inner class ViewHolder(private val binding: ItemTicketBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket)  {
            binding.txvTicketNb.text = itemView.context.getString(R.string.ticketNumber, ticket.ticketNumber)
            binding.txvTicketDate.text = ticket.createdDate
            binding.chipTicketPriority.text = TranslationHelper.ticketPriorityString(itemView.context, ticket.priority)
            binding.chipTicketPriority.chipBackgroundColor = ColorHelper.ticketPriorityColor(itemView.context, ticket.priority)
            binding.chipTicketState.text = TranslationHelper.ticketStatusColor(itemView.context, ticket.status)
            binding.chipTicketState.chipBackgroundColor = ColorHelper.ticketStatusColor(itemView.context, ticket.status)




        }
    }
}


