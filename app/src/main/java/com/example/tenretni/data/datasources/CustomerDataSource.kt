package com.example.tenretni.data.datasources

import com.example.tenretni.core.Constants
import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Gateway
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class CustomerDataSource: JsonDataSource() {

    fun installGateway(href:String, qr: String): Gateway {
        //Mettre en JSON
        //Envoie au serveur avec un POST
        val fullpath = "$href/gateways"
        val (_,_,result) = fullpath.httpPost().jsonBody(qr).responseJson()
        //Gérer la réponse
        return when(result){
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun retrieveCustomerGateways(href:String): List<Gateway> {
        val fullpath = "$href/gateways"
        val (_,_,result) = fullpath.httpGet().responseJson()
        //Gérer la réponse
        return when(result){
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
}