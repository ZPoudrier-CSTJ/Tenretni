package com.example.tenretni.core

import android.content.Context
import com.example.tenretni.R

object TranslationHelper {

    fun ticketPriorityString(context: Context, priority: String): String {
        return when (Constants.TicketPriority.valueOf(priority)) {
            Constants.TicketPriority.Low -> context.getString(R.string.txtPriorityLow)
            Constants.TicketPriority.Normal -> context.getString(R.string.txtPriorityNormal)
            Constants.TicketPriority.High -> context.getString(R.string.txtPriorityHigh)
            Constants.TicketPriority.Critical -> context.getString(R.string.txtPriorityCritical)
        }
    }

    fun ticketStatusString(context: Context, status: String): String {
        return when (Constants.TicketStatus.valueOf(status)) {
            Constants.TicketStatus.Open -> context.getString(R.string.txtStateOpen)
            Constants.TicketStatus.Solved -> context.getString(R.string.txtStateSolve)
        }

    }

    fun connectionStatusString(context: Context, status: String): String {
        return when (Constants.ConnectionStatus.valueOf(status)) {
            Constants.ConnectionStatus.Online -> context.getString(R.string.txtGatewayStateOnline)
            Constants.ConnectionStatus.Offline -> context.getString(R.string.txtGatewayStateOffline)
        }

    }
}