package com.example.tenretni.ui.gateways

import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.tickets.detail.DetailTicketUiState

sealed class DetailGatewayUiState {
    object Loading: DetailGatewayUiState()
    class Success(val gateway: Gateway): DetailGatewayUiState()
    class Error(val exception: Exception? = null) : DetailGatewayUiState()
}