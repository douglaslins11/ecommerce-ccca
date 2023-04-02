package com.lins.ecommerceccca

data class OrderDto (val id: Long, val quantity: Int)

data class CreateOrderInput(
    var cpf: String,
    var items: MutableList<OrderDto>,
    var coupon: String? = null,
    var from: String = "",
    var to: String  = ""
)

data class CreateOrderOutput (
    var message: String? = null,
    var total: Double = 0.0,
    var freight: Double = 0.0,
)
