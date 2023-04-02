package com.lins.ecommerceccca

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinMapperFactory
import java.time.LocalDateTime
import java.util.*
import kotlin.math.max

object Cli {

    fun execute() {
        val input = CreateOrderInput("", mutableListOf())
        while (true) {
            val scanner = Scanner(System.`in`)
            val command = scanner.nextLine()
            if (command.startsWith("set-cpf")) {
                input.apply { cpf = command.replace("set-cpf", "").trim() }
            }
            if (command.startsWith("add-item")) {
                val (idProduct, quantity) = command.replace("add-item ", "").split(" ")
                input.apply { items.add(OrderDto(idProduct.toLong(), quantity.toInt())) }
            }
            if (command.startsWith("checkout")) {
                val jdbiHandle = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "root", "root").open()
                jdbiHandle.registerRowMapper(KotlinMapperFactory())
                try {
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
                        val volume = (product.height/100) * (product.length/100) * (product.width/100)
                        val density = product.weight/volume
                        val itemFreight = 1000 * volume * (density/100)
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
                    if (input.from.isNotEmpty() && input.to.isNotEmpty()){
                        output.total += output.freight
                    }
                    println(output.toString())
                } catch (e: Error) {
                    println(e.message)
                } finally {
                    jdbiHandle.close()
                }
            }
        }
    }
}

fun main() {
    Cli.execute()
}