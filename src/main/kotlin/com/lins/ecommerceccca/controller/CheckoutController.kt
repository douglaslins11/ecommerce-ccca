package com.lins.ecommerceccca.controller

import com.lins.ecommerceccca.controller.dto.CreateOrderInput
import com.lins.ecommerceccca.controller.dto.CreateOrderOutput
import com.lins.ecommerceccca.controller.dto.OrderDto
import com.lins.ecommerceccca.model.Product
import com.lins.ecommerceccca.repository.CouponRepository
import com.lins.ecommerceccca.repository.ProductRepository
import com.lins.ecommerceccca.service.CpfAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checkout")
class CheckoutController(
    private val productRepository: ProductRepository,
    private val couponRepository: CouponRepository
) {

    @PostMapping
    fun createOrder(@RequestBody orderInput: CreateOrderInput): CreateOrderOutput {
        var output = CreateOrderOutput()
        val isValid = CpfAlgorithm.isValid(orderInput.cpf)
        if (isValid) {
            orderInput.items.forEach { item ->
                val product = productRepository.getOne(item.id)
                output.total += product.price * item.quantity
            }
        } else {
            output.message = "Invalid CPF"
        }
        orderInput.coupon?.let {
            val coupon = couponRepository.getOne(it)
            output.total -= (output.total * coupon.percentage)/100
        }
        return output
    }
}