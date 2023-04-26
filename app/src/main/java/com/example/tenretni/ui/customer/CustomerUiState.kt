package com.example.tenretni.ui.customer

import com.example.tenretni.domain.models.Customer
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.tickets.detail.DetailTicketUiState

sealed class CustomerUiState {
    object Loading: CustomerUiState()
    class Success(val gateways: List<Gateway>): CustomerUiState()
    class Error(val exception: Exception? = null) : CustomerUiState()
}