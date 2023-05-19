package com.lins.ecommerceccca

object FreightCalculator {

    fun calculate (product: Product) : Double {
        val volume = (product.height / 100) * (product.length / 100) * (product.width / 100)
        val density = product.weight / volume
        val itemFreight = 1000 * volume * (density / 100)
        return itemFreight
    }
}