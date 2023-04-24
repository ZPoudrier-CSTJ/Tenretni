package com.example.tenretni.data.repositories

import com.example.tenretni.core.ApiResult
import com.example.tenretni.core.Constants
import com.example.tenretni.data.datasources.GatewayDataSource
import com.example.tenretni.data.datasources.TicketDataSource
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GatewayRepository {

    private val gatewayDataSource = GatewayDataSource()

    fun retrieveAll() : Flow<ApiResult<List<Gateway>>> {
        return flow {
            while(true) {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(gatewayDataSource.retrieveAll()))
                } catch (ex:Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.Delay.CUSTOMER_GATEWAYS_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(href: String) : Flow<ApiResult<Gateway>> {
        return flow<ApiResult<Gateway>> {
            while (true){
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(gatewayDataSource.retrieveOne(href)))
                }catch (ex:Exception){
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.Delay.GATEWAY_DETAIL_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun updateOne(href: String, action: String) : Flow<ApiResult<Gateway>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(gatewayDataSource.updateOne(href, action)))
            }catch (ex:Exception){
                emit(ApiResult.Error(ex))
            }
        }
    }
}