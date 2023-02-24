package com.lins.ecommerceccca.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lins.ecommerceccca.controller.dto.CreateOrderInput
import com.lins.ecommerceccca.controller.dto.CreateOrderOutput
import com.lins.ecommerceccca.controller.dto.OrderDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CheckoutControllerTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext
    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `Não deve criar um pedido quando o CPF for inválido`() {
        val input = CreateOrderInput(
            "099.957.300-44", listOf(
                OrderDto(1L, 1),
                OrderDto(2L, 1),
                OrderDto(3L, 3)
            )
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals("Invalid CPF", output.message)
    }

    @Test
    fun `Deve criar um pedido vazio com o CPF válido`() {
        val input = CreateOrderInput(
            "515.089.870-84", emptyList()
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(0.0, output.total)
    }

    @Test
    fun `Deve criar um pedido com 3 produtos`() {
        val items = listOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(6090.0, output.total)
    }

    @Test
    fun `Deve criar um pedido com 3 produtos com cumpom de desconto`() {
        val items = listOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
            "VALE20"
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(4872.0, output.total)
    }
}