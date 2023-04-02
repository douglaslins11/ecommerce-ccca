package com.lins.ecommerceccca

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CheckoutTest {

    private lateinit var checkout: Checkout

    @BeforeEach
    fun setUp() {
        checkout = Checkout()
    }

    @Test
    fun `Não deve criar um pedido quando o CPF for inválido`() {
        val input = CreateOrderInput(
            "099.957.300-44", mutableListOf(
                OrderDto(1L, 1),
                OrderDto(2L, 1),
                OrderDto(3L, 3)
            )
        )
        val output = assertThrows<Error> { checkout.execute(input) }
        Assertions.assertEquals("Invalid CPF", output.message)
    }

    @Test
    fun `Deve criar um pedido vazio com o CPF válido`() {
        val input = CreateOrderInput(
            "515.089.870-84", mutableListOf()
        )
        val output = checkout.execute(input)
        Assertions.assertEquals(0.0, output.total)
    }

    @Test
    fun `Deve criar um pedido com 3 produtos`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items
        )
        val output = checkout.execute(input)
        Assertions.assertEquals(6090.0, output.total)
    }

    @Test
    fun `Deve criar um pedido com 3 produtos com cupom de desconto`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
            "VALE20",
        )
        val output = checkout.execute(input)
        Assertions.assertEquals(4872.0, output.total)
    }

    @Test
    fun `Não deve aplicar cupom de desconto expirado`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
            "VALE10",
        )
        val output = checkout.execute(input)
        Assertions.assertEquals(6090.0, output.total)
    }

    @Test
    fun `Não deve criar um pedido quando a quantidade de um item for negativa`() {
        val items = mutableListOf(
            OrderDto(1L, -1),
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
        )
        val output = assertThrows<Error> { checkout.execute(input) }
        Assertions.assertEquals("Invalid quantity", output.message)
    }

    @Test
    fun `Não deve criar um pedido quando o item estiver duplicado`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(1L, 1),
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
        )
        val output = assertThrows<Error> { checkout.execute(input) }
        Assertions.assertEquals("Duplicated item", output.message)
    }

    @Test
    fun `Deve criar um pedido com 1 produto calculando o frete`() {
        val items = mutableListOf(
            OrderDto(1L, 3),
        )
        val input = CreateOrderInput(
            cpf = "515.089.870-84",
            items = items,
            from = "22060030",
            to = "88015600"
        )
        val output = checkout.execute(input)
        Assertions.assertEquals(3090.0, output.total)
        Assertions.assertEquals(90.0, output.freight)
    }

    @Test
    fun `Não deve criar um pedido se o produto tiver alguma dimensão negativa`() {
        val items = mutableListOf(
            OrderDto(4L, 1),
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
        )
        val output = assertThrows<Error> { checkout.execute(input) }
        Assertions.assertEquals("Invalid dimension", output.message)
    }

    @Test
    fun `Deve criar um pedido com 1 produto calculando o frete com valor mínimo`() {
        val items = mutableListOf(
            OrderDto(3L, 1),
        )
        val input = CreateOrderInput(
            cpf = "515.089.870-84",
            items = items,
            from = "22060030",
            to = "88015600"
        )
        val output = checkout.execute(input)
        Assertions.assertEquals(40.0, output.total)
        Assertions.assertEquals(10.0, output.freight)
    }
}