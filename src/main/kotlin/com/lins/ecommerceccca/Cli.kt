package com.lins.ecommerceccca

import java.util.*

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
                try {
                    val checkout = Checkout()
                    val output = checkout.execute(input)
                    println(output.toString())
                } catch (e: Error){
                    println(e.message)
                }
            }
        }
    }
}

fun main() {
    Cli.execute()
}