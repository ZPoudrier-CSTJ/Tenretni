package com.example.tenretni.core

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
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

    fun ticketStatusColor(context: Context, status: String): String {
        return when (Constants.TicketStatus.valueOf(status)) {
            Constants.TicketStatus.Open -> context.getString(R.string.txtStateOpen)
            Constants.TicketStatus.Solved -> context.getString(R.string.txtStateSolve)
        }

    }
}