package com.lins.ecommerceccca

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinMapperFactory
import java.time.LocalDateTime
import kotlin.math.max

class Checkout {

    fun execute(input: CreateOrderInput): CreateOrderOutput {
        val jdbiHandle = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "root", "root").open()
        jdbiHandle.registerRowMapper(KotlinMapperFactory())
        val isValid = CpfAlgorithm.isValid(input.cpf)
        if (!isValid) throw Error("Invalid CPF")
        var output = CreateOrderOutput()
        val items = mutableListOf<Long>()
        input.items.forEach { item ->
            if (item.quantity <= 0) throw Error("Invalid quantity")
            if (items.contains(item.id)) throw Error("Duplicated item")
            val product = jdbiHandle.createQuery("select * from product where id = :id")
                .bind("id", item.id)
                .mapTo(Product::class.java)
                .findFirst()
                .get()
            if (product.length <= 0 || product.height <= 0 || product.width <= 0 || product.weight <= 0) throw Error("Invalid dimension")
            val volume = (product.height / 100) * (product.length / 100) * (product.width / 100)
            val density = product.weight / volume
            val itemFreight = 1000 * volume * (density / 100)
            output.freight += max(itemFreight, 10.0) * item.quantity
            output.total += product.price * item.quantity
            items.add(item.id)
        }
        input.coupon?.let {
            val coupon = jdbiHandle.createQuery("select * from coupon where code = :code")
                .bind("code", it)
                .mapTo(Coupon::class.java)
                .findFirst()
                .get()
            if (coupon.expireDate >= LocalDateTime.now()) {
                output.total -= (output.total * coupon.percentage) / 100
            }
        }
        if (input.from.isNotEmpty() && input.to.isNotEmpty()) {
            output.total += output.freight
        }
        jdbiHandle.close()
        return output
    }
}