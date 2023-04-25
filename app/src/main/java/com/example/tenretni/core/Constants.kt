package com.example.tenretni.core

object Constants {
    private const val SECOND = 1000L
    object BaseURL {
        //private const val BASE_API = "http://10.0.2.2:5000"
        private const val BASE_API = "https://api.andromia.science"
        const val TICKETS = "$BASE_API/tickets"
        const val CUSTOMERS = "$BASE_API/customers"
        const val GATEWAYS = "$BASE_API/gateways"
        const val NETWORK = "$BASE_API/network"
    }

    object Delay {
        const val TICKET_DELAY = SECOND * 30
        const val CUSTOMER_GATEWAYS_DELAY = SECOND * 30
        const val GATEWAY_DETAIL_DELAY = SECOND * 30
    }

    object Loading {
        const val LOADING_TIME = SECOND * 1
        const val LOADING_INCREMENT = SECOND * 1
    }

    const val FLAG_API_URL = "https://flagcdn.com/h40/%s.png"

    enum class TicketPriority {
        Low, Normal, High, Critical
    }

    enum class TicketStatus {
        Open, Solved
    }

    enum class ConnectionStatus {
        Online, Offline
    }

}