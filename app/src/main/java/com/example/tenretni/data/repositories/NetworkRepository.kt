package com.example.tenretni.data.repositories

import com.example.tenretni.core.ApiResult
import com.example.tenretni.core.Constants
import com.example.tenretni.data.datasources.GatewayDataSource
import com.example.tenretni.data.datasources.NetworkDataSource
import com.example.tenretni.domain.models.Network
import com.example.tenretni.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NetworkRepository {

    private val networkDataSource = NetworkDataSource()

    fun retrieveAll() : Flow<ApiResult<Network>> {
        return flow {
            while(true) {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(networkDataSource.retrieveAll()))
                } catch (ex:Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.Delay.TICKET_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }
}