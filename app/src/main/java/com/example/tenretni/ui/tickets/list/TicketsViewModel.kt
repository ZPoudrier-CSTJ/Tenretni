package com.example.tenretni.ui.tickets.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketsViewModel : ViewModel() {

    private val ticketRepository = TicketRepository()

    private val _ticketUiState = MutableStateFlow<TicketsUiState>(TicketsUiState.Loading)
    val ticketUiState = _ticketUiState.asStateFlow()

    init {
        refreshTickets()
    }

    fun refreshTickets() {
        viewModelScope.launch {
            ticketRepository.retrieveAll().collect {
                _ticketUiState.update { _ ->
                    when (it) {
                        is ApiResult.Error -> TicketsUiState.Error(it.exception)
                        ApiResult.Loading -> TicketsUiState.Loading
                        is ApiResult.Success -> TicketsUiState.Success(it.data)
                    }
                }
            }
        }
    }
}