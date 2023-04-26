package com.example.tenretni.ui.tickets.detail

import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.tickets.list.TicketsUiState

sealed class DetailTicketUiState {
    object Loading: DetailTicketUiState()
    class Success(val ticket: Ticket): DetailTicketUiState()
    class Error(val exception: Exception? = null) : DetailTicketUiState()
}