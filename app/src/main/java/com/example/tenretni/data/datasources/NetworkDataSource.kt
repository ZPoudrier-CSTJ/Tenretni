package com.example.tenretni.data.datasources

import com.example.tenretni.core.Constants
import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Network
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class NetworkDataSource: JsonDataSource() {
    fun retrieveAll(): Network {
        val (_, _, result) = Constants.BaseURL.NETWORK.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

}