package com.example.tenretni.ui.gateways

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.GatewayRepository
import com.example.tenretni.ui.customer.CustomerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailGatewayViewModel(private val href: String): ViewModel() {

    private val gatewayRepository = GatewayRepository()
    private val _detailGatewayUiState = MutableStateFlow<DetailGatewayUiState>(DetailGatewayUiState.Loading)

    val detailGatewayUiState = _detailGatewayUiState.asStateFlow()

    init {
        loadDetailGateway()
    }

    fun loadDetailGateway()
    {
        viewModelScope.launch {
            gatewayRepository.retrieveOne(href).collect{apiResult ->

                _detailGatewayUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> DetailGatewayUiState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailGatewayUiState.Loading
                        is ApiResult.Success -> DetailGatewayUiState.Success(apiResult.data)
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