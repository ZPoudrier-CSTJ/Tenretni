package com.example.tenretni.data.repositories

import com.example.tenretni.core.ApiResult
import com.example.tenretni.core.Constants
import com.example.tenretni.data.datasources.CustomerDataSource
import com.example.tenretni.domain.models.Gateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CustomerRepository {
    private val customerDataSource = CustomerDataSource()

    fun installGateway(href:String, qr: String): Flow<ApiResult<Gateway>> {
        return flow{
            try {
                emit(ApiResult.Success(customerDataSource.installGateway(href, qr)))
            }catch (ex: Exception){
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveCustomerGateways(href:String): Flow<ApiResult<List<Gateway>>> {
        return flow {
            while (true) {
                try {
                    emit(ApiResult.Success(customerDataSource.retrieveCustomerGateways(href)))
                } catch (ex: Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.Delay.GATEWAY_DETAIL_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }
}