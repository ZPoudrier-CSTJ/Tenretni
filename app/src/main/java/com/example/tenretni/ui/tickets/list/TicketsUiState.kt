package com.example.tenretni.ui.tickets.list

import com.example.tenretni.domain.models.Ticket

sealed class TicketsUiState {
    object Loading: TicketsUiState()
    class Success(val tickets: List<Ticket>): TicketsUiState()
    class Error(val exception: Exception? = null) : TicketsUiState()
}