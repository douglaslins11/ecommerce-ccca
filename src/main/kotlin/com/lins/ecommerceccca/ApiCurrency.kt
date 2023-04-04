package com.lins.ecommerceccca

//Substituindo uma Rest Api, só a título de estudos
object ApiCurrency {
    fun getCurrencies() = mapOf("USD" to 3.0 + Math.random())
}