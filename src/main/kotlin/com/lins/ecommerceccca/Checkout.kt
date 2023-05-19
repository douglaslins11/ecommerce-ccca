package com.lins.ecommerceccca

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.max

@Service
class Checkout (
    private val currencyGateway: CurrencyGateway,
    private val productRepository: ProductRepository,
    private val couponRepository: CouponRepository
) {

    fun execute(input: CreateOrderInput): CreateOrderOutput {
        val isValid = CpfAlgorithm.isValid(input.cpf)
        if (!isValid) throw Error("Invalid CPF")
        val output = CreateOrderOutput()
        val currencies = currencyGateway.getCurrencies()
        val items = mutableListOf<Long>()
        input.items.forEach { item ->
            if (item.quantity <= 0) throw Error("Invalid quantity")
            if (items.contains(item.id)) throw Error("Duplicated item")
            val product = productRepository.getProduct(item.id)
            if (product.length <= 0 || product.height <= 0 || product.width <= 0 || product.weight <= 0) throw Error("Invalid dimension")
            if (product.currency == "USD"){
                output.total += product.price * item.quantity * currencies["USD"]!!
            } else {
                output.total += product.price * item.quantity
            }
            val itemFreight = FreightCalculator.calculate(product)
            output.freight += max(itemFreight, 10.0) * item.quantity
            items.add(item.id)
        }
        input.coupon?.let {
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