package com.lins.ecommerceccca

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.max

@Service
class Checkout {

    fun execute(input: CreateOrderInput): CreateOrderOutput {
        val isValid = CpfAlgorithm.isValid(input.cpf)
        if (!isValid) throw Error("Invalid CPF")
        val output = CreateOrderOutput()
        val currencyGateway = CurrencyGatewayHttp()
        val currencies = currencyGateway.getCurrencies()
        val items = mutableListOf<Long>()
        input.items.forEach { item ->
            if (item.quantity <= 0) throw Error("Invalid quantity")
            if (items.contains(item.id)) throw Error("Duplicated item")
            val productRepository = ProductRepositoryDatabase()
            val product = productRepository.getProduct(item.id)
            if (product.length <= 0 || product.height <= 0 || product.width <= 0 || product.weight <= 0) throw Error("Invalid dimension")
            if (product.currency == "USD"){
                output.total += product.price * item.quantity * currencies["USD"]!!
            } else {
                output.total += product.price * item.quantity
            }
            val volume = (product.height / 100) * (product.length / 100) * (product.width / 100)
            val density = product.weight / volume
            val itemFreight = 1000 * volume * (density / 100)
            output.freight += max(itemFreight, 10.0) * item.quantity
            items.add(item.id)
        }
        input.coupon?.let {
            val couponRepository = CouponRepositoryDatabase()
            val coupon = couponRepository.getCoupon(it)
            if (coupon.expireDate >= LocalDateTime.now()) {
                output.total -= (output.total * coupon.percentage) / 100
            }
        }
        if (input.from.isNotEmpty() && input.to.isNotEmpty()) {
            output.total += output.freight
        }
        return output
    }
}