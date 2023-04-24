package com.example.tenretni.data.datasources

import com.example.tenretni.core.Constants
import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class GatewayDataSource:JsonDataSource() {

    fun retrieveAll(): List<Gateway> {
        val (_, _, result) = Constants.BaseURL.GATEWAYS.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
    //TODO: href or ticketId
    fun retrieveOne(href: String): Gateway {
        val (_,_, result) = href.httpGet().responseJson()

        return when (result){
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun updateOne(href: String, action: String): Gateway {
        //Envoie au serveur avec un POST
        //TODO: check if path GOOD
        val fullpath = "$href/actions?type=$action"
        val (_,_,result) = fullpath.httpPost().responseJson()
        //Gérer la réponse
        return when(result){
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
}