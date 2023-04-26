package com.example.tenretni.ui.tickets.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.CustomerRepository
import com.example.tenretni.data.repositories.TicketRepository
import com.example.tenretni.ui.customer.CustomerUiState
import com.example.tenretni.ui.tickets.list.TicketsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailTicketViewModel(private val href: String) : ViewModel() {
    private val ticketRepository = TicketRepository()
    private val customerRepository = CustomerRepository()
    private val _ticketUiState = MutableStateFlow<DetailTicketUiState>(DetailTicketUiState.Loading)
    private val _customerUiState = MutableStateFlow<CustomerUiState>(CustomerUiState.Loading)

    val detailTicketUiState = _ticketUiState.asStateFlow()
    //val customerUiState = _customerUiState.asStateFlow()


    init {
        viewModelScope.launch {
            ticketRepository.retrieveOne(href).collect{apiResult ->
                _ticketUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> DetailTicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailTicketUiState.Loading
                        is ApiResult.Success -> {
                            viewModelScope.launch {
                                customerRepository.retrieveCustomerGateways(apiResult.data.customer.href).collect{apiResultCustomer ->
                                     _customerUiState.update {
                                         when(apiResultCustomer){
                                             is ApiResult.Error -> CustomerUiState.Error(apiResultCustomer.exception)
                                             ApiResult.Loading -> CustomerUiState.Loading
                                             is ApiResult.Success -> CustomerUiState.Success(apiResultCustomer.data)
                                         }
                                     }

                                }
                            }
                            DetailTicketUiState.Success(apiResult.data)
                        }
                    }
                }
            }
        }
    }


    class Factory(private val href: String): ViewModelProvider.Factory{
        override fun<T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(href)
        }
    }




}