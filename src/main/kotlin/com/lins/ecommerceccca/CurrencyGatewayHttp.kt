package com.lins.ecommerceccca

class CurrencyGatewayHttp {

    fun getCurrencies(): Map<String, Double> {
        val response = ApiCurrency.getCurrencies()
        return response
    }
}