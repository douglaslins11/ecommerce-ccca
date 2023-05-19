package com.lins.ecommerceccca

import org.springframework.stereotype.Component

@Component
class CurrencyGatewayHttp : CurrencyGateway{
    override fun getCurrencies(): Map<String, Double> {
        return ApiCurrency.getCurrencies()
    }

}