package com.lins.ecommerceccca

interface CurrencyGateway {

    fun getCurrencies(): Map<String, Double>
}