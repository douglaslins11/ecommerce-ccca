package com.lins.ecommerceccca.controller.dto

data class OrderDto (val id: Long, val quantity: Int)

data class CreateOrderInput(val cpf: String, val items: List<OrderDto>, val coupon: String? = null)

data class CreateOrderOutput (var message: String? = null) {
    var total: Double = 0.0
}
