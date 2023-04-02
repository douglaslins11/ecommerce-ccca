package com.lins.ecommerceccca

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checkout")
class Api {

    @PostMapping
    fun createOrder(@RequestBody orderInput: CreateOrderInput): ResponseEntity<CreateOrderOutput> {
        return try {
            val checkout = Checkout()
            val output = checkout.execute(orderInput)
            ResponseEntity.ok(output)
        } catch (e: Error){
            ResponseEntity.unprocessableEntity().body(CreateOrderOutput(e.message))
        }
    }
}