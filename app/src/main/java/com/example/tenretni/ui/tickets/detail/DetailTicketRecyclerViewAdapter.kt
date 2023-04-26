package com.example.tenretni.ui.tickets.detail

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.ItemGatewaysBinding
import com.example.tenretni.domain.models.Gateway

class DetailTicketRecyclerViewAdapter(

    var gateways: List<Gateway> = listOf(),
            private val onGatewayClick: (Gateway) -> Unit) : RecyclerView.Adapter<DetailTicketRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailTicketRecyclerViewAdapter.ViewHolder {
        return ViewHolder(ItemGatewaysBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, ) {
        val gateway = gateways[position]
        holder.bind(gateway)

        holder.itemView.setOnClickListener {
            onGatewayClick(gateway)
        }
    }

    override fun getItemCount() = gateways.size

    inner class ViewHolder(private val binding: ItemGatewaysBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(gateway: Gateway){
            binding.chipGatewayStatus.chipBackgroundColor = ColorHelper.connectionStatusColor(itemView.context, gateway.connection.status)
            binding.chipGatewayStatus.text = TranslationHelper.connectionStatusString(itemView.context, gateway.connection.status)
            binding.txvSerial.text = gateway.serialNumber
            if(gateway.connection.status == "Online"){
                binding.txvNA.visibility = View.GONE
                binding.grpGatewayInfo.visibility = View.VISIBLE
                binding.txvPing.text = gateway.connection.ping.toString()
                binding.txvDownload.text = gateway.connection.download.toString()
                binding.txvUpload.text = gateway.connection.upload.toString()

            }
            else{
                binding.txvNA.visibility = View.VISIBLE
                binding.grpGatewayInfo.visibility = View.GONE
            }
        }
    }

}